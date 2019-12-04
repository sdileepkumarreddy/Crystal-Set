package edu.neu.csye6200.ca;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
 * @author: Dileep Reddy 
 * NUID: 001063317
 * Class Description: this class creates a logger along with file handler and returns it to the class
 * It a common class and can be accessed by all classes to create logger and file handler in one shot which gives us redundancy
 */

public class CustomLogger {

	int LOG_SIZE = 20000;
	int LOG_ROTATION_COUNT = 100;
	private Logger logger;
	private String className;

	public CustomLogger(String className) {
		this.className = className;
		logger = Logger.getLogger(className);

	}

	public Logger getLoggerAndFileHandler()
	{		
		try {		
			Handler handler;
			String sep = File.separator;
			String logPath = ".." + sep + "logs" + sep + className +".log";
			handler = new FileHandler(logPath, LOG_SIZE, LOG_ROTATION_COUNT);
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logger;
	}

}
