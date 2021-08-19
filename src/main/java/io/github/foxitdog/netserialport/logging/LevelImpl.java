package io.github.foxitdog.netserialport.logging;

import java.util.logging.Level;

/**
 * 在java logging Level的基础上添加熟悉的api
 * @author foxitdog
 */
public class LevelImpl extends Level{

    public static final Level ERROR = new LevelImpl("error", 999);
    public static final Level WARN = new LevelImpl("warn", 899);
    public static final Level INFO = new LevelImpl("info", 799);
    public static final Level DEBUG = new LevelImpl("debug", 499);
    
    public static void init(){}

    private LevelImpl(String name, int value) {
        super(name, value);
    }

    private static final long serialVersionUID = 1L;
    
}
