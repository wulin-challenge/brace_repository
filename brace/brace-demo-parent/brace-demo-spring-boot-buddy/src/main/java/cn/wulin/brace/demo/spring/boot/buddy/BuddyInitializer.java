package cn.wulin.brace.demo.spring.boot.buddy;

import java.lang.instrument.Instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.PriorityOrdered;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/**
 * byte-buddy集成spring boot的案例
 * 
 * @author wulin
 *
 */
@SuppressWarnings("rawtypes")
public class BuddyInitializer implements ApplicationContextInitializer, PriorityOrdered {
	private static final Logger LOGGER = LoggerFactory.getLogger(BuddyInitializer.class);

	public BuddyInitializer() {
		initBuddy();
	}

	private void initBuddy() {
		try {
			premain(null, ByteBuddyAgent.install());
		} catch (Exception e) {
			LOGGER.error("byte buddy 初始化失败!");
		}
	}

	public static void premain(String arg, Instrumentation inst) throws Exception {
		new AgentBuilder.Default().ignore(ElementMatchers.nameStartsWith("net.bytebuddy."))
				.type(ElementMatchers.nameEndsWith("DefaultListableBeanFactory"))
				.transform(new AgentBuilder.Transformer() {

					@Override
					public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription,
							ClassLoader classLoader, JavaModule module) {
						return builder.method(ElementMatchers.named("getMergedLocalBeanDefinition"))
								.intercept(MethodDelegation.to(MyInterceptor.class));
					}
				}).installOn(inst);
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		// 故意留空不做任何事情
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

}
