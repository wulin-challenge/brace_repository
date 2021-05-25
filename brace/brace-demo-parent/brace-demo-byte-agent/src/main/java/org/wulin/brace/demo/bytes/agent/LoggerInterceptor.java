package org.wulin.brace.demo.bytes.agent;

import java.util.List;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class LoggerInterceptor {
	public static String log(@SuperCall Callable<String> zuper) throws Exception {
		System.out.println("Calling database");
		try {
			return zuper.call();
		} finally {
			System.out.println("Returned from database");
		}
	}
}