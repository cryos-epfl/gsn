package gsn.vsensor;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import gsn.beans.DataField;
import gsn.beans.StreamElement;

import org.apache.log4j.Logger;

public class GPSSvToRawBinary extends BridgeVirtualSensorPermasense {
	
	private static String GPS_TIME_FIELD_NAME = "gps_unixtime";
	private static Short GPS_RAW_DATA_VERSION = 1;

	private static String GPS_ITOW_FIELD_NAME = "gps_time";
	private static String GPS_WEEK_FIELD_NAME = "gps_week";
	private static String GPS_NUMSV_FIELD_NAME = "num_sv";
	private static String GPS_CPMES_FIELD_NAME = "carrier_phase";
	private static String GPS_PRMES_FIELD_NAME = "pseudo_range";
	private static String GPS_DOMES_FIELD_NAME = "doppler";
	private static String GPS_SV_FIELD_NAME = "space_vehicle";
	private static String GPS_MESQI_FIELD_NAME = "measurement_quality";
	private static String GPS_CNO_FIELD_NAME = "signal_strength";
	private static String GPS_LLI_FIELD_NAME = "loss_of_lock";
	
	private static final transient Logger logger = Logger.getLogger(GPSSvToRawBinary.class);

	private static final DataField[] dataField = {
		new DataField("POSITION", "INTEGER"),
		new DataField("GENERATION_TIME", "BIGINT"),
		new DataField("TIMESTAMP", "BIGINT"),
		new DataField("DEVICE_ID", "INTEGER"),
		new DataField("GPS_UNIXTIME", "BIGINT"),

		new DataField("SENSOR_TYPE", "VARCHAR(16)"),
		new DataField("GPS_RAW_DATA_VERSION", "SMALLINT"),
		new DataField("GPS_SATS", "INTEGER"),
		new DataField("GPS_MISSING_SV", "TINYINT"),
		new DataField("GPS_RAW_DATA", "BINARY"),
		new DataField("QUEUE_SIZE", "INTEGER")};
	
	private Map<String,Map<Long,SvContainer>> inputStreamNameToSvMapList = Collections.synchronizedMap(new HashMap<String,Map<Long,SvContainer>>());
	private long bufferSizeInMs;
	
	@Override
	public boolean initialize() {
		boolean ret = super.initialize();
		
		String bufferSizeInDays = getVirtualSensorConfiguration().getMainClassInitialParams().get("buffer_size_in_days");
		try {
			bufferSizeInMs = Long.decode(bufferSizeInDays) * 86400000L;
		}
		catch (NumberFormatException e) {
			logger.error("buffer_size_in_days has to be an integer");
			return false;
		}
		
		return ret;
	}
	
	@Override
	public void dataAvailable(String inputStreamName, StreamElement data) {
		Long gps_unixtime = (Long)data.getData(GPS_TIME_FIELD_NAME);
		
		if (!inputStreamNameToSvMapList.containsKey(inputStreamName)) {
			inputStreamNameToSvMapList.put(inputStreamName, new HashMap<Long,SvContainer>());
		}
		
		Map<Long,SvContainer> svContainerMap = inputStreamNameToSvMapList.get(inputStreamName);
		
		SvContainer svContainer = svContainerMap.get(gps_unixtime);
		if (svContainer == null) {
			svContainer = new SvContainer((Byte)data.getData(GPS_NUMSV_FIELD_NAME));
		}
		
		try {
			if (svContainer.putSv(data)) {
				data = svContainer.getRawBinaryStream();
				svContainerMap.remove(gps_unixtime);
				data.setData(dataField[10].getName(), svContainerMap.size());
				super.dataAvailable(inputStreamName, data);
				
			}
			else {
				svContainerMap.put(gps_unixtime, svContainer);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		Iterator<Long> iter = svContainerMap.keySet().iterator();
		while (iter.hasNext()) {
			gps_unixtime = iter.next();
			if (gps_unixtime < System.currentTimeMillis()-bufferSizeInMs) {
				data = svContainer.getRawBinaryStream();
				svContainerMap.remove(gps_unixtime);
				data.setData(dataField[10].getName(), svContainerMap.size());
				super.dataAvailable(inputStreamName, data);
			}
		}
	}

	class SvContainer {
		private StreamElement[] streamElements;
		private Byte numSv;
		private Byte pointer;
		
		protected SvContainer(Byte numSv) {
			pointer = 0;
			this.numSv = numSv;
			streamElements = new StreamElement[numSv];
		}
		
		protected boolean putSv(StreamElement streamElement) throws Exception {
			if (pointer == numSv)
				throw new Exception("SvContainer already full!");
			streamElements[pointer++] = streamElement;
			if (pointer == numSv)
				return true;
			else
				return false;
		}
		
		protected Byte getNumSv() {
			return numSv;
		}
		
		protected StreamElement getRawBinaryStream() {
			ByteBuffer rxmRaw = ByteBuffer.allocate(16+24*pointer);
			rxmRaw.order(ByteOrder.LITTLE_ENDIAN);
			
			// RXM-RAW Header
			rxmRaw.put((byte) 0xB5);
			rxmRaw.put((byte) 0x62);
			
			// RXM-RAW ID
			rxmRaw.put((byte) 0x02);
			rxmRaw.put((byte) 0x10);
			
			// RXM-RAW Length
			rxmRaw.putShort((short) (24*pointer));
			
			// RXM-RAW Payload
			rxmRaw.putInt((Integer)streamElements[0].getData(GPS_ITOW_FIELD_NAME));
			rxmRaw.putShort((Short)streamElements[0].getData(GPS_WEEK_FIELD_NAME));
			rxmRaw.put((byte) (numSv & 0xFF));
			rxmRaw.put((byte) 0x00);
			for (short i=0; i<pointer; i++) {
				rxmRaw.putDouble((Double)streamElements[i].getData(GPS_CPMES_FIELD_NAME));
				rxmRaw.putDouble((Double)streamElements[i].getData(GPS_PRMES_FIELD_NAME));
				double d = (Double)streamElements[i].getData(GPS_DOMES_FIELD_NAME);
				rxmRaw.putFloat((float)d);
				rxmRaw.put((Byte)streamElements[i].getData(GPS_SV_FIELD_NAME));
				rxmRaw.put((byte)((Short)streamElements[i].getData(GPS_MESQI_FIELD_NAME)&0xFF));
				rxmRaw.put((Byte)streamElements[i].getData(GPS_CNO_FIELD_NAME));
				rxmRaw.put((Byte)streamElements[i].getData(GPS_LLI_FIELD_NAME));
			}
			
			// RXM-RAW Checksum
			byte CK_A = 0;
			byte CK_B = 0;
			for (int i=2; i<6+24*pointer; i++) {
				CK_A += rxmRaw.get(i);
				CK_B += CK_A;
			}
			rxmRaw.put(CK_A);
			rxmRaw.put(CK_B);
			
			return new StreamElement(dataField, new Serializable[]{
					streamElements[0].getData(dataField[0].getName()),
					streamElements[0].getData(dataField[1].getName()),
					streamElements[0].getData(dataField[2].getName()),
					streamElements[0].getData(dataField[3].getName()),
					streamElements[0].getData(dataField[4].getName()),
					streamElements[0].getData(dataField[5].getName()),
					GPS_RAW_DATA_VERSION,
					(int)numSv,
					(byte)(numSv-pointer),
					rxmRaw.array(),
					null});
		}
	}
}