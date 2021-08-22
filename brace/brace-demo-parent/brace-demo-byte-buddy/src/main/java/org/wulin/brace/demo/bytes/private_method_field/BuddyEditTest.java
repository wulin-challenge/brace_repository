package org.wulin.brace.demo.bytes.private_method_field;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.wulin.brace.demo.bytes.private_method_field.impl.MyFactory;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/**
 * 修改属性,真强私有方法
 * @author wubo
 *
 */
public class BuddyEditTest {
	private final static String[] DRUID_CLASS_PREFIX = new String[]{"com.alibaba.druid.",
			"io.seata.sqlparser.druid.","xxx.xxx.xxx"};

	public static void main(String[] args) throws Exception {
        premain(null, ByteBuddyAgent.install());
        MyFactory myFactory = new MyFactory();
//        Pair<String[], URL[]> pair = myFactory.get();
//        
//        System.out.println("left:"+pair.getKey()+", right: "+pair.getValue());
        
        myFactory.print();
        System.out.println();
    }
	
	public interface MyType extends ModifierContributor.ForField{
		 int MASK = Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED | Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC
	                | Opcodes.ACC_DEPRECATED | Opcodes.ACC_ENUM | Opcodes.ACC_FINAL | Opcodes.ACC_STATIC
	                | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_TRANSIENT | Opcodes.ACC_VOLATILE;
	}

    
    public static void premain(String arg, Instrumentation inst) throws Exception {
        new AgentBuilder.Default()
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy."))
                //.enableBootstrapInjection(inst,temp)
                .type(ElementMatchers.nameEndsWith("MyClassLoaderTarget"))
                .transform(new AgentBuilder.Transformer() {

					@Override
					public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription,ClassLoader classLoader, JavaModule module) {
						 return builder
						.method(ElementMatchers.named("getDruidUrls").or(ElementMatchers.named("print")))
						.intercept(MethodDelegation.to(MyPriviteInterceptor.class));
					}
                }).installOn(inst);
    }

    public static class MyPriviteInterceptor {
    	
    	

    	@RuntimeType
        public static Object intercept(@This(optional=true) Object o,@FieldValue(value="DRUID_CLASS_PREFIX") Object obj,@Origin Method method,@SuperCall Callable<Object> zuper) throws Exception {
    		
            System.out.println("Intercepted!");
            
            Class<?> declaringClass = method.getDeclaringClass();
            
            Method print2 = declaringClass.getDeclaredMethod("print2", String.class);
            print2.setAccessible(true);
            for (String string : DRUID_CLASS_PREFIX) {
            	 print2.invoke(o, string);
			}
           
            
            
            Object call = zuper.call();
            
            if(call instanceof URL[]) {
            	URL[] urls = (URL[]) call;
            	
            	
            	List<URL> newUrls = new ArrayList<>();
            	
            	for (URL url : urls) {
            		newUrls.add(url);
				}
            	
            	newUrls.add(new URL("http://new_add_url"));
            	call = newUrls.toArray(new URL[0]);
            	
            }
            
            return call;
        }
    }
}
