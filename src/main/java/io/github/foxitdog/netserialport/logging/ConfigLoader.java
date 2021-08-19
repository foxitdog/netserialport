package io.github.foxitdog.netserialport.logging;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import org.apache.commons.io.FileUtils;

/**
 * 日志配置加载
 * @author foxitdog
 */
public class ConfigLoader {
    
    public ConfigLoader(){
        LevelImpl.init();
        try (InputStream is = FileUtils.openInputStream(new File("./log.properties"))){
            // 加载项目中的配置文件
            LogManager.getLogManager().readConfiguration(is);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

}
