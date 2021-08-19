package io.github.foxitdog.netserialport;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import io.github.foxitdog.netserialport.impl.UncaughtExceptionHandlerImpl;
import io.github.foxitdog.netserialport.logging.LogManager;
import io.github.foxitdog.netserialport.logging.LoggerImpl;

/**
 * 启动类
 *
 * @author 梁云
 */
public class Main {
	/**
	 * log必须在系统属性java.util.logging.config.class设置后进行手动加载，才能进入我们的
	 */
	public static LoggerImpl log;

	public static void main(String[] args) throws InterruptedException, IOException, SQLException {
		// java logging 日志配置加载
		System.setProperty("java.util.logging.config.class", "io.github.foxitdog.netserialport.logging.ConfigLoader");
		
		initEnv();
		
		log = LogManager.getLogger(Main.class.getName());

		log.info("start app");

		App app = new App();
		
		app.run(args);

		log.info("app is started");
	}

	private static void initEnv() {
		// 日志文件夹
		File logDir = new File("log/");
		if (!logDir.isDirectory()) {
			logDir.mkdirs();
		}
		// 系统错误的全局异常捕捉
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandlerImpl());
	}

}
