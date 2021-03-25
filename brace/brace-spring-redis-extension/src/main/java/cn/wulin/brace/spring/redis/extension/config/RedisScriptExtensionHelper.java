package cn.wulin.brace.spring.redis.extension.config;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 这是redis基于lua脚本实现的工具类
 * @author wulin
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class RedisScriptExtensionHelper {
	/**
	 * 脚本缓存
	 */
	private static final ConcurrentHashMap<String,RedisScript> SCRIPT_CACHE = new ConcurrentHashMap<String,RedisScript>();

	@Autowired
	@Qualifier("extensionRedisTemplate")
	private RedisTemplate<Object,Object> redisTemplate;
	
	/**
	 * 执行lua脚本命令
	 * @param scriptFileName 脚本的文件名称
	 * @param returnTypeClass 返回值类型
	 * @param keys 对应的参数key集合
	 * @param args 对应的参数值集合
	 * @return 返回执行结果命令
	 */
	public <T> T execute(String scriptFileName,Class<T> returnTypeClass,List<Object> keys, Object... args){
		RedisScript<T> loadScript = loadScript(scriptFileName, returnTypeClass);
		T result = redisTemplate.execute(loadScript, keys, args);
		return result;
	}
	
	/**
	 * 加载在 META-INF/scripts/* 下面的脚本文件
	 * @param scriptFileName lua脚本文件名称
	 * @param returnTypeClass 返回值类型
	 * @return 返回对应的redis脚本
	 */
	public <T> RedisScript<T> loadScript(String scriptFileName,Class<T> returnTypeClass){
		RedisScript redisScript = SCRIPT_CACHE.get(scriptFileName);
		
		if(redisScript != null) {
			return redisScript;
		}
		
		redisScript = loadScript0(scriptFileName, returnTypeClass);
		SCRIPT_CACHE.putIfAbsent(scriptFileName, redisScript);
		return redisScript;
	}
	
	/**
	 * 加载在 META-INF/scripts/* 下面的脚本文件
	 * @param scriptFileName lua脚本文件名称
	 * @param returnTypeClass 返回值类型
	 * @return
	 */
	private <T> RedisScript<T> loadScript0(String scriptFileName,Class<T> returnTypeClass){
		ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("META-INF/scripts/"+scriptFileName+".lua"));
		DefaultRedisScript<T> defaultRedisScript = new DefaultRedisScript<T>();
		defaultRedisScript.setScriptSource(scriptSource);
		defaultRedisScript.setResultType(returnTypeClass);
		return defaultRedisScript;
	}
	
}
