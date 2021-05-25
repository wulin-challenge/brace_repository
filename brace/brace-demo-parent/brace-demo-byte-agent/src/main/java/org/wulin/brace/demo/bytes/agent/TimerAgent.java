package org.wulin.brace.demo.bytes.agent;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

public class TimerAgent {
  public static void premain(String arguments, Instrumentation instrumentation) {
	  
	  System.out.println("执行代理了!!!!!!!!!!!!");
      new AgentBuilder.Default()
      .type(ElementMatchers.nameEndsWith("Timed"))
      .transform(new TransformerImpl())
      .installOn(instrumentation);
      
  }
  
  public static class TransformerImpl implements Transformer{

	@Override
	public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,JavaModule module) {
		return builder.method(ElementMatchers.any())
				.intercept(MethodDelegation.to(TimingInterceptor.class));
	}
	  
  }
}