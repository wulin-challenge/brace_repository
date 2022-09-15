package cn.wulin.brace.jdk.compiler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;

/**
 * In-memory compile Java source code as String.
 * 
 * @author michael
 */
public class JavaStringCompiler {

	JavaCompiler compiler;
	StandardJavaFileManager stdManager;

	public JavaStringCompiler() {
		this.compiler = ToolProvider.getSystemJavaCompiler();
		//this.stdManager = compiler.getStandardFileManager(null, null, null);
		this.stdManager = getStandardFileManager(null, null, null);
	}
	
	/**
	 * 自定义Java文件管理器
	 *
	 * @param var1
	 * @param var2
	 * @param var3
	 * @return
	 */
	public SpringJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> var1, Locale var2, Charset var3) {
		Context var4 = new Context();
		var4.put(Locale.class, var2);
		if (var1 != null) {
			var4.put(DiagnosticListener.class, var1);
		}

		PrintWriter var5 = var3 == null ? new PrintWriter(System.err, true) : new PrintWriter(new OutputStreamWriter(System.err, var3), true);
		var4.put(Log.outKey, var5);
		return new SpringJavaFileManager(var4, true, var3);
	}

	/**
	 * Compile a Java source file in memory.
	 * 
	 * @param fileName
	 *            Java file name, e.g. "Test.java"
	 * @param source
	 *            The source code as String.
	 * @return The compiled results as Map that contains class name as key,
	 *         class binary as value.
	 * @throws IOException
	 *             If compile error.
	 */
	public Map<String, byte[]> compile(String fileName, String source) throws IOException {
		try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
			JavaFileObject javaFileObject = manager.makeStringSource(fileName, source);
			CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
			Boolean result = task.call();
			if (result == null || !result.booleanValue()) {
				throw new RuntimeException("Compilation failed.");
			}
			return manager.getClassBytes();
		}
	}

	/**
	 * Load class from compiled classes.
	 * 
	 * @param name
	 *            Full class name.
	 * @param classBytes
	 *            Compiled results as a Map.
	 * @return The Class instance.
	 * @throws ClassNotFoundException
	 *             If class not found.
	 * @throws IOException
	 *             If load error.
	 */
	public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
		
		MemoryClassLoader instance = MemoryClassLoader.getInstance();
		instance.addClass(classBytes);
		return instance.loadClass(name);
		
//		try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
//			return classLoader.loadClass(name);
//		}
	}
}
