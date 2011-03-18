package gsn.wrappers.tinyos;

/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'SensorscopeMaintenance'
 * message type.
 */

public class SensorscopeMaintenance extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 8;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 138;

    /** Create a new SensorscopeMaintenance of size 8. */
    public SensorscopeMaintenance() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new SensorscopeMaintenance of the given data_length. */
    public SensorscopeMaintenance(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SensorscopeMaintenance with the given data_length
     * and base offset.
     */
    public SensorscopeMaintenance(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SensorscopeMaintenance using the given byte array
     * as backing store.
     */
    public SensorscopeMaintenance(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SensorscopeMaintenance using the given byte array
     * as backing store, with the given base offset.
     */
    public SensorscopeMaintenance(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SensorscopeMaintenance using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public SensorscopeMaintenance(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SensorscopeMaintenance embedded in the given message
     * at the given base offset.
     */
    public SensorscopeMaintenance(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SensorscopeMaintenance embedded in the given message
     * at the given base offset and length.
     */
    public SensorscopeMaintenance(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <SensorscopeMaintenance> \n";
      try {
        s += "  [ntw_sender_id=0x"+Long.toHexString(get_ntw_sender_id())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [ntw_cost_to_bs=0x"+Long.toHexString(get_ntw_cost_to_bs())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [tsp_hop_count=0x"+Long.toHexString(get_tsp_hop_count())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [tsp_packet_sn=0x"+Long.toHexString(get_tsp_packet_sn())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [timestamp_offset=0x"+Long.toHexString(get_timestamp_offset())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: ntw_sender_id
    //   Field type: short, unsigned
    //   Offset (bits): 0
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'ntw_sender_id' is signed (false).
     */
    public static boolean isSigned_ntw_sender_id() {
        return false;
    }

    /**
     * Return whether the field 'ntw_sender_id' is an array (false).
     */
    public static boolean isArray_ntw_sender_id() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'ntw_sender_id'
     */
    public static int offset_ntw_sender_id() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'ntw_sender_id'
     */
    public static int offsetBits_ntw_sender_id() {
        return 0;
    }

    /**
     * Return the value (as a short) of the field 'ntw_sender_id'
     */
    public short get_ntw_sender_id() {
        return (short)getUIntBEElement(offsetBits_ntw_sender_id(), 8);
    }

    /**
     * Set the value of the field 'ntw_sender_id'
     */
    public void set_ntw_sender_id(short value) {
        setUIntBEElement(offsetBits_ntw_sender_id(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'ntw_sender_id'
     */
    public static int size_ntw_sender_id() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'ntw_sender_id'
     */
    public static int sizeBits_ntw_sender_id() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: ntw_cost_to_bs
    //   Field type: short, unsigned
    //   Offset (bits): 8
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'ntw_cost_to_bs' is signed (false).
     */
    public static boolean isSigned_ntw_cost_to_bs() {
        return false;
    }

    /**
     * Return whether the field 'ntw_cost_to_bs' is an array (false).
     */
    public static boolean isArray_ntw_cost_to_bs() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'ntw_cost_to_bs'
     */
    public static int offset_ntw_cost_to_bs() {
        return (8 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'ntw_cost_to_bs'
     */
    public static int offsetBits_ntw_cost_to_bs() {
        return 8;
    }

    /**
     * Return the value (as a short) of the field 'ntw_cost_to_bs'
     */
    public short get_ntw_cost_to_bs() {
        return (short)getUIntBEElement(offsetBits_ntw_cost_to_bs(), 8);
    }

    /**
     * Set the value of the field 'ntw_cost_to_bs'
     */
    public void set_ntw_cost_to_bs(short value) {
        setUIntBEElement(offsetBits_ntw_cost_to_bs(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'ntw_cost_to_bs'
     */
    public static int size_ntw_cost_to_bs() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'ntw_cost_to_bs'
     */
    public static int sizeBits_ntw_cost_to_bs() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: tsp_hop_count
    //   Field type: short, unsigned
    //   Offset (bits): 16
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'tsp_hop_count' is signed (false).
     */
    public static boolean isSigned_tsp_hop_count() {
        return false;
    }

    /**
     * Return whether the field 'tsp_hop_count' is an array (false).
     */
    public static boolean isArray_tsp_hop_count() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'tsp_hop_count'
     */
    public static int offset_tsp_hop_count() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'tsp_hop_count'
     */
    public static int offsetBits_tsp_hop_count() {
        return 16;
    }

    /**
     * Return the value (as a short) of the field 'tsp_hop_count'
     */
    public short get_tsp_hop_count() {
        return (short)getUIntBEElement(offsetBits_tsp_hop_count(), 8);
    }

    /**
     * Set the value of the field 'tsp_hop_count'
     */
    public void set_tsp_hop_count(short value) {
        setUIntBEElement(offsetBits_tsp_hop_count(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'tsp_hop_count'
     */
    public static int size_tsp_hop_count() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'tsp_hop_count'
     */
    public static int sizeBits_tsp_hop_count() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: tsp_packet_sn
    //   Field type: short, unsigned
    //   Offset (bits): 24
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'tsp_packet_sn' is signed (false).
     */
    public static boolean isSigned_tsp_packet_sn() {
        return false;
    }

    /**
     * Return whether the field 'tsp_packet_sn' is an array (false).
     */
    public static boolean isArray_tsp_packet_sn() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'tsp_packet_sn'
     */
    public static int offset_tsp_packet_sn() {
        return (24 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'tsp_packet_sn'
     */
    public static int offsetBits_tsp_packet_sn() {
        return 24;
    }

    /**
     * Return the value (as a short) of the field 'tsp_packet_sn'
     */
    public short get_tsp_packet_sn() {
        return (short)getUIntBEElement(offsetBits_tsp_packet_sn(), 8);
    }

    /**
     * Set the value of the field 'tsp_packet_sn'
     */
    public void set_tsp_packet_sn(short value) {
        setUIntBEElement(offsetBits_tsp_packet_sn(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'tsp_packet_sn'
     */
    public static int size_tsp_packet_sn() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'tsp_packet_sn'
     */
    public static int sizeBits_tsp_packet_sn() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: timestamp_offset
    //   Field type: long, unsigned
    //   Offset (bits): 32
    //   Size (bits): 32
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'timestamp_offset' is signed (false).
     */
    public static boolean isSigned_timestamp_offset() {
        return false;
    }

    /**
     * Return whether the field 'timestamp_offset' is an array (false).
     */
    public static boolean isArray_timestamp_offset() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'timestamp_offset'
     */
    public static int offset_timestamp_offset() {
        return (32 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'timestamp_offset'
     */
    public static int offsetBits_timestamp_offset() {
        return 32;
    }

    /**
     * Return the value (as a long) of the field 'timestamp_offset'
     */
    public long get_timestamp_offset() {
        return (long)getUIntBEElement(offsetBits_timestamp_offset(), 32);
    }

    /**
     * Set the value of the field 'timestamp_offset'
     */
    public void set_timestamp_offset(long value) {
        setUIntBEElement(offsetBits_timestamp_offset(), 32, value);
    }

    /**
     * Return the size, in bytes, of the field 'timestamp_offset'
     */
    public static int size_timestamp_offset() {
        return (32 / 8);
    }

    /**
     * Return the size, in bits, of the field 'timestamp_offset'
     */
    public static int sizeBits_timestamp_offset() {
        return 32;
    }

}
