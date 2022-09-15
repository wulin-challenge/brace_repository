package cn.wulin.brace.jdk.compiler;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;

import javax.tools.JavaFileManager.Location;

import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;

/**
 * java 文件管理器 主要用来 重新定义class loader
 */
class SpringJavaFileManager extends JavacFileManager {


	public SpringJavaFileManager(Context context, boolean b, Charset charset) {
		super(context, b, charset);
	}


	@Override
	public ClassLoader getClassLoader(Location location) {
		nullCheck(location);
		ListBuffer var3 = new ListBuffer();
		Enumeration<URL> resources;
		try {
			resources = this.getClass().getClassLoader().getResources("");
			while(resources.hasMoreElements()) {
				URL nextElement = resources.nextElement();
				var3.add(nextElement);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		Iterable var2 = this.getLocation(location);
		if (var2 == null) {
			return null;
		} else {
			
			Iterator var4 = var2.iterator();

			while (var4.hasNext()) {
				File var5 = (File) var4.next();

				try {
					var3.append(var5.toURI().toURL());
				} catch (MalformedURLException var7) {
					throw new AssertionError(var7);
				}
			}
			return this.getClassLoader((URL[]) var3.toArray(new URL[var3.size()]));
		}
	}

	@Override
	protected ClassLoader getClassLoader(URL[] var1) { 
		//ClassLoader var2 = this.getClass().getClassLoader();
		ClassLoader var2 = MemoryClassLoader.getInstance();
		try {
			Class loaderClass = Class.forName("org.springframework.boot.loader.LaunchedURLClassLoader");
			Class[] var4 = new Class[]{URL[].class, ClassLoader.class};
			Constructor var5 = loaderClass.getConstructor(var4);
			
			ClassLoader springLoader = (ClassLoader) var5.newInstance(var1, var2);
			return springLoader;
		} catch (Throwable var6) {
		}
		return new URLClassLoader(var1, var2);
	}


}