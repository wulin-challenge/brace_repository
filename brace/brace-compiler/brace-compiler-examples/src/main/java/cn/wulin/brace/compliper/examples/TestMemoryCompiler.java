package cn.wulin.brace.compliper.examples;


import java.lang.reflect.Method;

import cn.wulin.brace.jdk.compiler.JavaStringCompiler;
import cn.wulin.brace.jdk.compiler3.CompilerLoaderUtil3;

/**
 * 测试内存编译
 * 
 * @author ThinkPad
 *
 */
public class TestMemoryCompiler {

	public static void main(String[] args) {
		try {
			JavaStringCompiler jsc = new JavaStringCompiler();
			
//			Class<?> zz = com.sun.tools.javac.main.Main.class;
//			JdkCompiler2 jdk = new JdkCompiler2();

			String code1 = code1();
			
			Class<?> clazz1 = CompilerLoaderUtil3.compilerLoader("cn.wulin.brace.compliper.examples.stringcode.StringCodeUtil", code1);

//			Map<String, byte[]> compile1 = jsc.compile("StringCodeUtil.java", code1);
//			
//			Class<?> clazz1 = jsc.loadClass("cn.wulin.brace.compliper.examples.stringcode.StringCodeUtil", compile1);
//			
//			Class<?> clazz1 = jdk.compile(code1, TestMemoryCompiler.class.getClassLoader());
			
			System.out.println(clazz1);
			System.out.println(clazz1);
			System.out.println(clazz1);
			
			String code2 = code2();
////
////			Map<String, byte[]> compile2 = jsc.compile("TestStringCode.java", code2);
////			
////			Class<?> clazz2 = jsc.loadClass("cn.wulin.brace.compliper.examples.stringcode.TestStringCode", compile2);
////			
//			Class<?> clazz2 = jdk.compile(code2, TestMemoryCompiler.class.getClassLoader());
			
			Class<?> clazz2 = CompilerLoaderUtil3.compilerLoader("cn.wulin.brace.compliper.examples.stringcode.TestStringCode", code2);
			Object newInstance = clazz2.newInstance();
			
			Method method = clazz2.getMethod("code");
			
			Object invoke = method.invoke(newInstance);
			System.out.println(invoke);
			System.out.println(clazz2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String code2() {
		
		return "package cn.wulin.brace.compliper.examples.stringcode;\r\n" + 
				"\r\n" + 
				"/**\r\n" + 
				" * 测试字符串代码\r\n" + 
				" * @author wulin\r\n" + 
				" *\r\n" + 
				" */\r\n" + 
				"public class TestStringCode {\r\n" + 
				"\r\n" + 
				"	public String code() {\r\n" + 
				"		StringCodeUtil su = new StringCodeUtil();\r\n" + 
				"		String s = \"123\"+su.xx();\r\n" + 
				"		\r\n" + 
				"		System.out.println(s);\r\n" + 
				"		return s;\r\n" + 
				"	}\r\n" + 
				"}";
	}

	private static String code1() {
		return "package cn.wulin.brace.compliper.examples.stringcode;\r\n" + 
				"\r\n" + 
				"import org.apache.commons.lang3.StringUtils;\r\n" + 
				"\r\n" + 
				"/**\r\n" + 
				" * \r\n" + 
				" * @author wulin\r\n" + 
				" *\r\n" + 
				" */\r\n" + 
				"public class StringCodeUtil {\r\n" + 
				"\r\n" + 
				"	public String xx() {\r\n" + 
				"		\r\n" + 
				"		int compare = StringUtils.compare(\"aabb\", \"ddd\");\r\n" + 
				"		\r\n" + 
				"		System.out.println(\"compare : \"+ compare);\r\n" + 
				"		return \"StringCodeUtil\";\r\n" + 
				"	}\r\n" + 
				"}\r\n" + 
				"";
	}
}
