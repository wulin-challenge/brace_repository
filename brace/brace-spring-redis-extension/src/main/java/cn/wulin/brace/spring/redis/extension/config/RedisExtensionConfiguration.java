package cn.wulin.brace.spring.redis.extension.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisExtensionConfiguration {

	private RedisSerializer<Object> defaultRedisSerializer;

	@Bean
	public RedisTemplate<Object, Object> extensionRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		
		if (this.defaultRedisSerializer != null) {
			template.setDefaultSerializer(this.defaultRedisSerializer);
		}
		template.setConnectionFactory(connectionFactory);
		return template;
	}

	@Autowired(required = false)
	@Qualifier("defaultRedisSerializer")
	public void setDefaultRedisSerializer(RedisSerializer<Object> defaultRedisSerializer) {
		this.defaultRedisSerializer = defaultRedisSerializer;
	}

	@Bean
	public RedisScriptExtensionHelper redisScriptExtensionHelper() {
		return new RedisScriptExtensionHelper();
	}
	
	@Bean
	public RedisExtensionCommand redisExtensionCommand() {
		return new RedisExtensionCommand();
	}

}
