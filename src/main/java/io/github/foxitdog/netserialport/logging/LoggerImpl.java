package io.github.foxitdog.netserialport.logging;

import java.util.logging.Logger;

import io.github.foxitdog.netserialport.utils.StringUtils;


/**
 * LoggerImpl log4j2的logger样式的接口
 * 
 * @author foxitdog
 */
public class LoggerImpl {

    public static final int ERROR = LevelImpl.ERROR.intValue();
    public static final int WARN = LevelImpl.WARN.intValue();
    public static final int INFO = LevelImpl.INFO.intValue();
    public static final int DEBUG = LevelImpl.DEBUG.intValue();

    public static final String ERROR_STRING = "error";
    public static final String WARN_STRING = "warn";
    public static final String INFO_STRING = "info";
    public static final String DEBUG_STRING = "debug";

    /** default pattern is "{}" */
    public static volatile String ParamPattern = "{}";

    volatile Logger log;
    volatile int state;

    public LoggerImpl(String name) {
        log = Logger.getLogger(name);
    }

    public boolean isInfoEnabled() {
        return log.isLoggable(LevelImpl.INFO);
    }

    public boolean isDebugEnabled() {
        return log.isLoggable(LevelImpl.DEBUG);
    }

    public boolean isWarnEnabled() {
        return log.isLoggable(LevelImpl.WARN);
    }

    public boolean isErrorEnabled() {
        return log.isLoggable(LevelImpl.ERROR);
    }

    public void info(String message, Object... params) {
        info(message, params, 1);
    }

    private void info(String message, Object[] params, int trackindex) {
        if (!isInfoEnabled()) {
            return;
        }
        out(StringUtils.paramReplace(ParamPattern, message, params), INFO_STRING, trackindex + 1, null);
    }

    public void debug(String message, Object... params) {
        debug(message, params, 1);
    }

    private void debug(String message, Object[] params, int trackindex) {
        if (isDebugEnabled()) {
            out(StringUtils.paramReplace(ParamPattern, message, params), DEBUG_STRING, trackindex + 1, null);
        }
    }

    public void error(String message, Object... params) {
        error(message, params, 1, null);
    }

    public void error(String message, Exception e) {
        error(message, null, 1, e);
    }

    private void error(String message, Object[] params, int trackindex, Throwable e) {
        if (isErrorEnabled()) {
            out(StringUtils.paramReplace(ParamPattern, message, params), ERROR_STRING, trackindex + 1, e);
        }
    }

    public void warn(String message, Object... params) {
        warn(message, params, 1);
    }

    private void warn(String message, Object[] params, int trackindex) {
        if (isWarnEnabled()) {
            out(StringUtils.paramReplace(ParamPattern, message, params), WARN_STRING, trackindex + 1, null);
        }
    }

    public void out(String message, String type, int trackIndex, Throwable e) {
        switch (type) {
        case INFO_STRING:
            log.log(LevelImpl.INFO, message);
            break;
        case DEBUG_STRING:
            log.log(LevelImpl.DEBUG, message);
            break;
        case WARN_STRING:
            log.log(LevelImpl.WARN, message);
            break;
        case ERROR_STRING:
            log.log(LevelImpl.ERROR, message, e);
            break;
        default:
            break;
        }
    }
}