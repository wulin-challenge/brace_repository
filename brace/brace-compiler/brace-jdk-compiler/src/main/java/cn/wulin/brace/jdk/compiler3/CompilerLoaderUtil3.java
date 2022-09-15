package cn.wulin.brace.jdk.compiler3;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

/**
 * 实现java 字节码的编译和加载
 * @author wulin
 *
 */
public class CompilerLoaderUtil3 {
	private static MemoryClassLoader3 classLoader = MemoryClassLoader3.getInstrance();
	
	/**
	 * 这个安装器不用,但是一定要初始化,且不能删除,切记切记,因为重新加载class时,需要这个代理
	 */
	private static Instrumentation install = ByteBuddyAgent.install();

	/**
	 * 编译并加载指定类的class,该方法支持同一 clazzName 的重复加载
	 * @param className
	 * @param code
	 * @return
	 */
	public static synchronized Class<?> compilerLoader(String className,String code){
		try {
			Class<?> findClass = findClass(className);
			byte[] codeByte = classLoader.registerJava(className, code);
			if(findClass == null) {
				//说明jvm中还不存在该类
				Class<?> clazz = classLoader.findClass(className);
				return clazz;
			}else {
				
				//说明jvm中已经存在该类,这里是覆盖该类的class,即使该类已经被创建了实例,对应的实例也会受到影响
				MyForClassLoader loader = new MyForClassLoader(classLoader,codeByte);
				new ByteBuddy()
				  .redefine(findClass, loader)
				  .make()
				  .load(classLoader, ClassReloadingStrategy.fromInstalledAgent());
				return findClass;
			}
		} catch (Throwable e) {
			throw new RuntimeException("编译加载"+className+"失败!",e);
		}
	}
	
	/**
	 * 判断类是否存在
	 * @param className
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public static Class<?> findClass(String className) {
		try {
			return classLoader.findClass(className);
		} catch (Exception e) {
			return null;
		}
	}
}
