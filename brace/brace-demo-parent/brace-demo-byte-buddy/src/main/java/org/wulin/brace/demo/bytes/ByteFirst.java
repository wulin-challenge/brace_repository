package org.wulin.brace.demo.bytes;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteFirst {

	public static void main(String[] args) {
		System.out.println("111");
		new AgentTimed().execute("wulin ");
	}
	
	public void printe(String name) {
		System.out.println("name : "+name);
	}
	
	@Test
	public void helloword() throws InstantiationException, IllegalAccessException {
		Class<?> dynamicType = new ByteBuddy()
				  .subclass(Object.class)
				  .method(ElementMatchers.named("toString"))
				  .intercept(FixedValue.value("Hello World!"))
				  .make()
				  .load(getClass().getClassLoader())
				  .getLoaded();
		
				assertThat(dynamicType.newInstance().toString(), is("Hello World!"));
	}
	
//	@Test
//	public void interceptor() throws InstantiationException, IllegalAccessException {
//		Class<? extends java.util.function.Function> dynamicType = new ByteBuddy()
//				  .subclass(java.util.function.Function.class)
//				  .method(ElementMatchers.named("apply"))
//				  .intercept(MethodDelegation.to(new TimingInterceptor()))
//				  .make()
//				  .load(getClass().getClassLoader())
//				  .getLoaded();
//				assertThat((String) dynamicType.newInstance().apply("Byte Buddy"), is("Hello from Byte Buddy"));
//	}
//	
//	@Test
//	public void interceptor2() throws InstantiationException, IllegalAccessException {
//		Class<? extends java.util.function.Function> dynamicType = new ByteBuddy()
//				.subclass(java.util.function.Function.class)
//				.method(ElementMatchers.named("apply"))
//				.intercept(MethodDelegation.to(new TimingInterceptor()))
//				.make()
//				.load(getClass().getClassLoader())
//				.getLoaded();
//		assertThat((String) dynamicType.newInstance().apply("Byte Buddy"), is("Hello from Byte Buddy"));
//	}
}
