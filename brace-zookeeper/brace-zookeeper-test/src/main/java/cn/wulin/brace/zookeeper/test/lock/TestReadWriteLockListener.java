package cn.wulin.brace.zookeeper.test.lock;

import java.util.concurrent.atomic.AtomicInteger;

import cn.wulin.brace.zookeeper.locks.RefreshNotify;
import cn.wulin.brace.zookeeper.locks.SharedReentrantReadWriteLock;

public class TestReadWriteLockListener {
	
	public static final String lock = "/locks/listener";
	
	//模式数据库数据
	private static AtomicInteger db = new AtomicInteger();
	
	private RefreshNotify listener = new RefreshNotifyListener();
	//模拟读客户端
	private SharedReentrantReadWriteLock readClientLock = new SharedReentrantReadWriteLock(lock);
	//模拟写客户端
	private SharedReentrantReadWriteLock writeClientLock = new SharedReentrantReadWriteLock(lock,listener);
	
	
	private static class RefreshNotifyListener extends RefreshNotify{
		@Override
		public void doRefreshNotify() {
			System.out.println("doRefreshNotify");
		}
    }
	public static void main(String[] args) {
		
	}
	
	private static class ReadWriteLockRefresh{
		
	}
}
