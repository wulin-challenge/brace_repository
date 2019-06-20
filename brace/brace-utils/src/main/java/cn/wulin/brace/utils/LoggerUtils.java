package cn.wulin.brace.utils;

import java.util.concurrent.ConcurrentHashMap;

import cn.wulin.ioc.logging.Logger;
import cn.wulin.ioc.logging.LoggerFactory;

/**
 * 日志工具类
 * @author wubo
 */
public class LoggerUtils {
    /**
     * log对象
     */
	private final static ConcurrentHashMap<Class<?>, Logger> loggers= new ConcurrentHashMap<>();
	
	public static final Logger getLogger() {
		RuntimeException re = new RuntimeException();
		StackTraceElement[] stackTrace = re.getStackTrace();
		
		if(stackTrace[2] != null) {
			String className = stackTrace[2].getClassName();
			Class<?> clazz = null;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			
			if(loggers.containsKey(clazz)) {
				return loggers.get(clazz);
			}
			
			loggers.put(clazz, LoggerFactory.getLogger(clazz));
			return loggers.get(clazz);
		}
		throw new RuntimeException("获取Logger对象失败!");
	}
    
    /**
     * 日志信息
     * @param message 输出信息
     */
    final public static void info(String message) {
    	getLogger().info(message);
    }
    /**
     * 日志信息
     * @param message 输出信息
     */
    final public static void info(String message,Throwable e) {
    	getLogger().info(message,e);
    }
    /**
     * 调试信息
     * @param message 输出信息
     */
    final public static void debug(String message) {
    	getLogger().debug(message);
    }
    /**
     * 调试信息
     * @param message 输出信息
     */
    final public static void debug(String message,Throwable e) {
    	getLogger().debug(message,e);
    }
    /**
     * 错误信息
     * @param message 输出信息
     */
    final public static void error(String message) {
    	getLogger().error(message);
    }
    
    /**
     * 错误信息
     * @param message 输出信息
     */
    final public static void error(String message,Throwable e) {
    	getLogger().error(message,e);
    }
    /**
     * 警告信息
     * @param message 输出信息
     */
    final public static void warn(String message,Throwable e) {
    	getLogger().warn(message,e);
    }
    /**
     * 警告信息
     * @param message 输出信息
     */
    final public static void warn(String message) {
    	getLogger().warn(message);
    }
    
    /**
     * 严重错误信息
     * @param message 输出信息
     */
    final public static void fatal(String message) {
//    	getLogger().fatal(message);
    }

}
