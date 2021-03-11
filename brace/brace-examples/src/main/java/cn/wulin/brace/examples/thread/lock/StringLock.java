package cn.wulin.brace.examples.thread.lock;

import java.util.UUID;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

/**
 * String 作为锁对象的案例
 * @author wulin
 *
 */
public class StringLock {
	private static Interner<String> stringPool = Interners.newWeakInterner();

	public static void main(String[] args) {
		String lockMinitor = UUID.randomUUID().toString().replaceAll("-", "");
		
		
		Thread thread = new Thread(()->{
			lock(lockMinitor+"");
		});
		thread.start();
		
		lock(lockMinitor+"");
		
		System.out.println();
	}
	
	private static void lock(String lockMinitor) {
		synchronized (stringPool.intern(lockMinitor)) {
			System.out.println(11);
		}
		
	}

}
