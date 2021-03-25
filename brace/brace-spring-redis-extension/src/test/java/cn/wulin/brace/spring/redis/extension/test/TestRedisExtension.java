package cn.wulin.brace.spring.redis.extension.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.wulin.brace.spring.redis.extension.config.RedisExtensionCommand;

@SpringBootTest(classes=DefaultStarter.class)
@RunWith(SpringRunner.class)
public class TestRedisExtension {
	
	@Autowired
	private RedisExtensionCommand redisExtensionCommand;

	@Test
	public void checkAndSet_demo() {
		Boolean checkAndSet_demo = redisExtensionCommand.checkAndSet_demo("aa:1", 1, 3);
		System.out.println(checkAndSet_demo);
	
	}
	
	@Test
	public void checkAndSet() {
		Boolean checkAndSet_demo = redisExtensionCommand.checkAndSet_demo("aa:2", 1, 2);
		System.out.println(checkAndSet_demo);
	
	}
}
