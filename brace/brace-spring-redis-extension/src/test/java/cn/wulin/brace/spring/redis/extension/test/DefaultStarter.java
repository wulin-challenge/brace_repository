package cn.wulin.brace.spring.redis.extension.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import cn.wulin.brace.spring.redis.extension.config.RedisExtensionConfiguration;

@EnableAutoConfiguration
@Import({RedisExtensionConfiguration.class})
public class DefaultStarter {

	public static void main(String[] args) {
		SpringApplication.run(DefaultStarter.class, args);
	}
}
