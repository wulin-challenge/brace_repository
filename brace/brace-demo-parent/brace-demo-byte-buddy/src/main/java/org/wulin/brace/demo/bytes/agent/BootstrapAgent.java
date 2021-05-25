package org.wulin.brace.demo.bytes.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.util.Collections;
import java.util.concurrent.Callable;

import org.wulin.brace.demo.bytes.AgentTimed;

import cn.wulin.ioc.extension.Activate;
import cn.wulin.ioc.extension.SPI;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.RawMatcher;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

public class BootstrapAgent {

    public static void main(String[] args) throws Exception {
        premain(null, ByteBuddyAgent.install());
//        HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://www.google.com").openConnection();
//        System.out.println(urlConnection.getRequestMethod());
        
        new AgentTimed().execute("wulin ");
    }

    
    public static void premain(String arg, Instrumentation inst) throws Exception {
//        File temp = Files.createTempDirectory("tmp").toFile();
//        ClassInjector.UsingInstrumentation.of(temp, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, inst).inject(Collections.singletonMap(
//                new TypeDescription.ForLoadedType(MyInterceptor.class),
//                ClassFileLocator.ForClassLoader.read(MyInterceptor.class).resolve()));
        new AgentBuilder.Default()
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy."))
                //.enableBootstrapInjection(inst,temp)
//                .type(ElementMatchers.nameEndsWith("Timed"))
                .type(ElementMatchers.isAnnotatedWith(SPI.class))
                .or(ElementMatchers.isAnnotatedWith(Activate.class))
                .transform(new AgentBuilder.Transformer() {

					@Override
					public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription,ClassLoader classLoader, JavaModule module) {
						 return builder.method(ElementMatchers.any()).intercept(MethodDelegation.to(MyInterceptor.class));
					}
                }).installOn(inst);
    }

    public static class MyInterceptor {

    	@RuntimeType
        public static String intercept(@SuperCall Callable<String> zuper) throws Exception {
            System.out.println("Intercepted!");
            return zuper.call();
        }
    }
}