package wantgoo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jUtil {
	Logger logger;
	static String filePath = "C:/wantgoo_app/log4j.properties";
	
	public Log4jUtil() {
		Properties logp = new Properties();
	    try {
	    	InputStream in = new BufferedInputStream (new FileInputStream(filePath));
	    	logp.load(in);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }

	    PropertyConfigurator.configure(logp);
	    logger = Logger.getLogger(Log4jUtil.class);
	}
	
	public void writeInfo(String msg) {
//		logger.debug("Debug message.");
//	    logger.info(logger.getClass().getName());
//	    logger.info("Info message.");
//	    logger.warn("Warn message."); 
//	    logger.error("Error message.");
	    
		
	    logger.info(msg);
	}
	
	public void writeWarn(String msg) {
		logger.warn(msg); 
	}
	
	public void writeError(String msg) {
		logger.error(msg); 
	}
}
