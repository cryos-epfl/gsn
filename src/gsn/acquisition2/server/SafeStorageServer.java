package gsn.acquisition2.server;

import gsn.acquisition2.SafeStorage;
import gsn.acquisition2.SafeStorageDB;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.*;
import org.apache.mina.common.*;
import org.apache.mina.filter.codec.*;
import org.apache.mina.filter.codec.serialization.*;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.*;

public class SafeStorageServer {
	
	public static final byte SS_START_MODE   = 1;
	public static final byte SS_CLEAN_MODE   = 100;
	
	public static transient Logger logger = Logger.getLogger(SafeStorageServer.class);
	
	private static final String DEFAULT_SAFESTORAGE_LOG4J_PROPERTIES = "conf/log4j_safestorage.properties";
	
	private IoAcceptor acceptor;
	
	public SafeStorageServer(int portNo) throws IOException, ClassNotFoundException, SQLException {
		SafeStorage ss  = new SafeStorage(portNo);
		acceptor = new SocketAcceptor();
		acceptor.getDefaultConfig().setThreadModel(ThreadModel.MANUAL);
				
		// Prepare the service configuration.
		SocketAcceptorConfig cfg = new SocketAcceptorConfig();
		cfg.setReuseAddress(true);
		ObjectSerializationCodecFactory oscf = new ObjectSerializationCodecFactory();
	    oscf.setDecoderMaxObjectSize(oscf.getEncoderMaxObjectSize());	    
	    cfg.getFilterChain().addLast("codec",   new ProtocolCodecFilter(oscf));
	    // Create an unbounded Thread pool
	    ThreadPoolExecutor tpe = new ThreadPoolExecutor (0, Integer.MAX_VALUE, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>()) ;	    
	    cfg.getFilterChain().addLast("threadPool", new ExecutorFilter(tpe));
	    
	    logger.debug("MINA Decoder MAX: " + oscf.getDecoderMaxObjectSize() + " MINA Encoder MAX: " + oscf.getEncoderMaxObjectSize());
	    acceptor.bind(new InetSocketAddress(portNo),   new SafeStorageServerSessionHandler(ss), cfg);
		logger.info("Safe Storage Server is listening on port: " + portNo);
		
		
		
	}
  
  public void shutdown () {
	  acceptor.unbindAll();
  }
  
  public static void main(String[] args) throws Exception {
	PropertyConfigurator.configure ( DEFAULT_SAFESTORAGE_LOG4J_PROPERTIES );    
	int safeStorageServerPort = Integer.parseInt(args[0]);
	int safeStorageControllerPort = Integer.parseInt(args[1]);
	byte safeStorageMode = Byte.parseByte(args[2]);
	switch (safeStorageMode) {
		case SS_START_MODE : {
			SafeStorageServer sss = new SafeStorageServer(safeStorageServerPort);
			new SafeStorageController(sss, safeStorageControllerPort);
			break;
		}
		case SS_CLEAN_MODE : {
			SafeStorageDB storage = new SafeStorageDB(safeStorageServerPort);
			storage.dropAllTables();
			logger.warn("SafeStorage database is now clean and empty.");
			break;
		}
		default : logger.error("Not valid SafeStorage mode >" + safeStorageMode + "<");
	}
  }
}
