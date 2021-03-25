package cn.wulin.brace.spring.redis.extension.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * redis的扩展命令
 * @author wulin
 */
public class RedisExtensionCommand {
	@Autowired
	private RedisScriptExtensionHelper redisScriptExtensionHelper;
	
	/**
	 * 检测值并设置值,这是一个demo实现,主要是用来学习使用
	 * @param key 
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public Boolean checkAndSet_demo(String key,Object oldValue,Object newValue) {
		return redisScriptExtensionHelper.execute("checkAndSetDemo", Boolean.class, Arrays.asList(key), oldValue,newValue);
	}
	
	/**
	 * 检测值并设置值
	 * @param key 
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public Boolean checkAndSet(String key,Object oldValue,Object newValue) {
		return redisScriptExtensionHelper.execute("checkAndSet", Boolean.class, Arrays.asList(key), oldValue,newValue);
	}

}
