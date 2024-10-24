package task1.test;

public class Logger {
	private static Logger instance;
	
	public enum LoggerLevel{
		OFF,
		INFO,
		WARNING,
		DEBUG,
		ERROR
	}
	
	private LoggerLevel level;
	
	static {
		instance = new Logger(TestEchoServer.LOG_LEVEL);
	}
	
	public Logger(LoggerLevel level) {
		this.level = level;
	}
	
	public static void info(String string) {
		if(instance.level != LoggerLevel.OFF) {
			System.out.println("INFO    :" + "" + string);
		}
	}
	
	public static void warning(String string) {
		if(instance.level != LoggerLevel.OFF || instance.level != LoggerLevel.INFO) {
			System.out.println("WARNING :" + "" + string);
		}
	}
	
	public static void debug(String string) {
		if(instance.level == LoggerLevel.DEBUG || instance.level == LoggerLevel.ERROR) {
			System.out.println("DEBUG   :" + "" + string);
		}
	}
	
	public static void error(String string) {
		if(instance.level == LoggerLevel.ERROR) {
			System.out.println("ERROR   :" + "" + string);
		}
	}
	
	public static synchronized void setLogLevel(LoggerLevel logLevel) {
		instance.level = logLevel;
	}
}
