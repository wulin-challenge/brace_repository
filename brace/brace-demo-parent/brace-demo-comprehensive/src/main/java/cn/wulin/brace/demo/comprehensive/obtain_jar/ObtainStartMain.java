package cn.wulin.brace.demo.comprehensive.obtain_jar;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import cn.wulin.brace.utils.PathUtil;

/**
 * 过得启动jar的main方法
 * 
 * @author wulin
 *
 */
@Component
public class ObtainStartMain implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		
		String mainClassName = getMainClassName();
		Class<?> forName = Class.forName(mainClassName);
		
		String jarPath = PathUtil.getJarPath();
		System.out.println("1111 : "+jarPath);
		
		String jarPath2 = PathUtil.getJarPath(forName);
		System.out.println("2222 : "+jarPath2);
		
		String classpath = forName.getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println("3333 : "+classpath);
		
		String classpath2 = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		System.out.println(classpath2);
	}

	public static String getMainClassName() {
		StackTraceElement[] stackTraceElements = new RuntimeException().getStackTrace();
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			if ("main".equals(stackTraceElement.getMethodName())) {
				return stackTraceElement.getClassName();
			}
		}
		return "";
	}

}
