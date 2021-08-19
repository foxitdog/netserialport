package io.github.foxitdog.netserialport.logging;

/**
 * 获取logger
 * @author foxitdog
 * */
public class LogManager {
    public static LoggerImpl getLogger(String name){
        return new LoggerImpl(name);
    }
}
