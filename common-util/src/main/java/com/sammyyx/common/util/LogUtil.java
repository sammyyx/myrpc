package com.sammyyx.common.util;

import org.apache.logging.log4j.Logger;

/**
 * Log 工具类
 */
public class LogUtil {

    public static void info(Logger logger, String format, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(format, args);
        }
    }

    public static void info(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void error(Logger logger, Throwable throwable, String format, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(format, args, throwable);
        }
    }

    public static void error(Logger logger, Throwable throwable, String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message, throwable);
        }
    }
}
