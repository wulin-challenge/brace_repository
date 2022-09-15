package cn.wulin.brace.jni.test;

import cn.wulin.brace.jni.HelloJni;
import cn.wulin.brace.jni.LibraryUtil;

public class JniTest {
	
	public static void main(String[] args) {
		String path = JniTest.class.getClassLoader().getResource("windows").getPath();
//		String path = JniTest.class.getClassLoader().getResource(".").getPath();
		
		LibraryUtil.loadLibraryDirs(path, "dll");
		
		HelloJni hello = new HelloJni();
		
		String say = hello.say("wu222 lin");
		
		System.out.println(say);
		
	}
	

}
