package io.github.foxitdog.netserialport.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import io.github.foxitdog.netserialport.impl.DefaultThreadFactory;
import io.github.foxitdog.netserialport.utils.eventbus.Publisher;
import io.github.foxitdog.netserialport.utils.eventbus.impl.CommonPublisher;

import org.apache.commons.io.FileUtils;

import lombok.AllArgsConstructor;
import lombok.CustomLog;

/**
 * @author foxitdog
 */
@CustomLog
public class Constants {

	/* static final field */
	public static final Charset CHARSET = Charset.defaultCharset();
	
	public static final String PASSWORD = "password";

	public static SerialportConfig[] serialportConfigs;

	/**
	 * 启动时间
	 */
	public static volatile long startTime = System.currentTimeMillis();

	public static final ScheduledThreadPoolExecutor STPE = new ScheduledThreadPoolExecutor(10,
			new DefaultThreadFactory());

	public static final Publisher<String, Object> PUBLISHER = new CommonPublisher<>();

	public static final String EVENT_TO_SERIALPORT = "EVENT_TO_SERIALPORT";
	public static final String EVENT_TO_SERVER = "EVENT_TO_SERVER";

	public static String HOST = "192.168.3.59";
	public static int PORT = 8079;

	static {
		Properties config = new Properties();
		try {
			File configfile = new File("./config.properties");
			if (configfile.isFile()) {
				config.load(FileUtils.openInputStream(configfile));
				HOST = config.getProperty("server.host");
				PORT = Integer.parseInt(config.getProperty("server.port"));
				int num = Integer.parseInt(config.getProperty("sp.num"));
				serialportConfigs = new SerialportConfig[num];
				for (int i = 0; i < num; i++) {
					serialportConfigs[i]=getSerialportConfig(config, i+1);
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			System.exit(0);
		}
	}

	static SerialportConfig getSerialportConfig(Properties prop,int id){
		String prefix = "sp"+id;
		String enable = prop.getProperty(prefix+".enable", "false");
		String port = prop.getProperty(prefix+".port", "COM16");
		String baudrate = prop.getProperty(prefix+".baudrate", "9600");
		String databit = prop.getProperty(prefix+".databit", "8");
		String stopbit = prop.getProperty(prefix+".stopbit", "1");
		String parity = prop.getProperty(prefix+".parity", "0");
		String no = prop.getProperty(prefix+".no", "0");
		return new SerialportConfig(id,Boolean.parseBoolean(enable), port, Integer.parseInt(baudrate), Integer.parseInt(databit), Integer.parseInt(stopbit), Integer.parseInt(parity),Byte.parseByte(no));
	}
	public static void init() {
	};

	@AllArgsConstructor
	public static class SerialportConfig {
		/**
		 * 是否启用
		 */
		public int id;
		/**
		 * 是否启用
		 */
		public boolean enable;
		/**
		 * 端口
		 */
		public String port;
		/** 
		 * 波特率 
		 */
		public int baudrate;
		/**
		 * 数据位
		 */
		public int databit;
		/**
		 * 停止位
		 */
		public int stopbit;
		/**
		 * 校验位
		 */
		public int parity;
		public byte no;
	}
}
