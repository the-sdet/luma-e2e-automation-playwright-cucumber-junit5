package io.github.the_sdet.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class implements the logger functionality, that can be used throughout the project for Logging
 *
 * @author Pabitra Swain (pabitra.swain.work@gmail.com)
 */
@SuppressWarnings("unused")
public class Log {
    //Initialize Log4j instance
    private static final Logger Log = LogManager.getLogger(Log.class);

    //Info Level Logs
    public static void info(String message) {
        Log.info(message);
    }

    //Warn Level Logs
    public static void warn(String message) {
        Log.warn(message);
    }

    //Error Level Logs
    public static void error(String message) {
        Log.error(message);
    }

    //Error Level Logs
    public static void error(String message, Exception e) {
        Log.error(message, e);
    }

    //Fatal Level Logs
    public static void fatal(String message) {
        Log.fatal(message);
    }

    //Debug Level Logs
    public static void debug(String message) {
        Log.debug(message);
    }
}