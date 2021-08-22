package cn.wulin.brace.demo.starter.jar;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import org.wulin.basic.container.boot.PlatformStarter;

public class SpringStarter {
	public static void main(String[] args) throws IOException {
		System.out.println("-----main");
		PlatformStarter.start(args);
	}
	
	 public static void premain(String arg, Instrumentation inst) throws Exception {
		 System.out.println("---main"+inst);
	 }
	

}
