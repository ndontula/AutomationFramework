package com.automation.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

/**
 * This class provides Utility Helper functions for the Logging Module
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class LoggingUtils {

	/**
	 * Determines the name to be assigned to a logger. This should only be
	 * called by the EclipseLogger Constructor The function returns the name of
	 * the third function from the top in stack trace as that should be the name
	 * of the function creating the logger index[0] getNameForLogger index[1]
	 * EclipseLogger Constructor index[2] Function creating the logger
	 *
	 * @return the name for logger
	 */
	public static String getNameForLogger() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		// Following value is hardcoded because the calling function will always
		// be at stack frame 3.
		StackTraceElement e = stacktrace[3];
		String methodName = e.getMethodName();
		return methodName;
	}

	/**
	 * Init the logging module. The function checks if a logging configuration.
	 * file was passed via command line In case it finds a command line
	 * configuration, it uses that file to initialize the logging module. in
	 * case NO logging configuration is provided, it uses a default logging
	 * configuration
	 *
	 * @throws IOException
	 *             the io exception
	 */
	/*
	 * public static void init() throws IOException {
	 * 
	 * if(!StringUtils.isEmpty(CommandLineParser.getCommandLineParser().
	 * getLoggingConfig())) { ConfigurationSource c = new
	 * ConfigurationSource(new
	 * FileInputStream(CommandLineParser.getCommandLineParser().getLoggingConfig
	 * ())); LoggerContext ctx = Configurator.initialize(null, c);
	 * ctx.updateLoggers(); } }
	 */

	/**
	 * Gets logger.
	 *
	 * @return the logger
	 */
	public static Logger getLogger() {
		String loggerName = LoggingUtils.getNameForLogger();
		return LogManager.getLogger(loggerName);
	}

	public static Logger getNamedLogger(String name) {
		return LogManager.getLogger(name);
	}

	/**
	 * Sets log level.
	 *
	 * @param logger
	 *            the logger
	 * @param level
	 *            the level
	 */
	public static void setLogLevel(Logger logger, Level level) {
		Configurator.initialize(logger.getName(), String.valueOf(level));
	}

	/**
	 * Sets log level.
	 *
	 * @param logger
	 *            the logger
	 * @param level
	 *            the level
	 */
	public static void setLogLevel(Logger logger, String level) {
		Level logLevel = Level.ERROR;
		switch (level.toUpperCase()) {
		case "ERROR":
			break;
		case "DEBUG":
			logLevel = Level.DEBUG;
			break;
		case "OFF":
			logLevel = Level.OFF;
			break;
		case "WARNING":
			logLevel = Level.WARN;
			break;
		case "INFO":
			logLevel = Level.INFO;
			break;
		case "FATAL":
			logLevel = Level.FATAL;
			break;
		case "ALL":
			logLevel = Level.ALL;
			break;
		case "TRACE":
			logLevel = Level.TRACE;
			break;
		default:
			break;

		}
		Configurator.initialize(logger.getName(), String.valueOf(logLevel));
	}

	/**
	 * Sets logging file name.
	 */
	public static void setLoggingFileName() {
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		URL url = LoggingUtils.class.getResource("/logging/log4j2.xml");
		try {
			context.setConfigLocation(url.toURI());
		} catch (URISyntaxException e) {
		}

	}

	/**
	 * Captures browser log.
	 *
	 * @param fileName
	 *            file name
	 * @param driver
	 *            web driver
	 * @param logger
	 *            logger instance
	 * @return true if log captured successfully, else false
	 */
	public static boolean captureBrowserLog(String fileName, WebDriver driver, Logger logger) {
		File fout = new File(fileName);

		try (FileOutputStream fos = new FileOutputStream(fout);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {

			LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
			for (LogEntry entry : logEntries) {
				bw.write(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
				;
				bw.newLine();
			}

			logEntries = driver.manage().logs().get(LogType.CLIENT);
			for (LogEntry entry : logEntries) {
				bw.write(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
				;
				bw.newLine();
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
