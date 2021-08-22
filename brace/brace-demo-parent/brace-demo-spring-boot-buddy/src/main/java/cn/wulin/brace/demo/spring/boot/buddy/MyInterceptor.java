package cn.wulin.brace.demo.spring.boot.buddy;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.support.RootBeanDefinition;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

public class MyInterceptor {
		 
		    private static final Set<Class<?>> exclude = new HashSet<Class<?>>();
		    
//		    static {
//		    	exclude.add(FreeMarkerConfigurerPropertySet.class);
//		    	exclude.add(ServletContextAware.class);
//		    }
		    
		    /**
		     * 匹配返回true,否则返回false
		     * @param clazz
		     * @return
		     */
		    private static boolean match(Class<?> clazz) {
		    	for (Class<?> class1 : exclude) {
					if(class1.isAssignableFrom(clazz)) {
						return true;
					}
				}
		    	
		    	String packageString = clazz.getPackage().getName();
		    	
		    	if(packageString.startsWith("com.bjhy") || packageString.startsWith("org.apel")) {
		    		return false;
		    	}else {
		    		return true;
		    	}
		    }

//		    @RuntimeType
//	        public static Object intercept(@This(optional=true) Object o,@FieldValue(value="DRUID_CLASS_PREFIX") Object obj,@Origin Method method,@SuperCall Callable<Object> zuper) throws Exception {
		    
	    	@RuntimeType
	        public static Object intercept(@This(optional=true) Object me,@Origin Method method,@SuperCall Callable<Object> zuper) throws Exception {
	    		Object call = zuper.call();
	    		if(call instanceof RootBeanDefinition) {
	    			RootBeanDefinition beanDefinition = (RootBeanDefinition) call;
	    			
	    			if(beanDefinition.hasBeanClass()) {
	    				Class<?> beanClass = beanDefinition.getBeanClass();
	    				
	    				if(!match(beanClass)) {
		    				beanDefinition.setLazyInit(true);
		    			}
	    			}
	    		}
	            return call;
	        }
	    }