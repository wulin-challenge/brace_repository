package cn.wulin.brace.zookeeper.test.listener.lock1;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.wulin.brace.zookeeper.locks.ReadWriteCallback;
import cn.wulin.brace.zookeeper.locks.RefreshNotify;
import cn.wulin.brace.zookeeper.locks.SharedReentrantReadWriteLock;

public class TestSimpleReadWriteLockListener {
	
	public static Integer common_testData;
	
	public static final String lock = "/locks/rwl";
	
	private RefreshNotify listener = new RefreshNotifyListener();
	//模拟读客户端
	private SharedReentrantReadWriteLock readClientLock = new SharedReentrantReadWriteLock(TestSimpleReadWriteLockListener.lock);
	//模拟写客户端
	private SharedReentrantReadWriteLock writeClientLock = new SharedReentrantReadWriteLock(TestSimpleReadWriteLockListener.lock,listener);
			
	
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-temp1-1.xml");
		ctx.start();
		
		TestSimpleReadWriteLockListener rwl = new TestSimpleReadWriteLockListener();
		final ReadWriteLockTest rwlt = rwl.new ReadWriteLockTest();
		final ReadWriteLockTest rwlt2 = rwl.new ReadWriteLockTest();
		
		multiClient(rwlt,rwlt2);
		
		System.in.read();
	}

	//模拟多客户端
	private static void multiClient(final ReadWriteLockTest rwlt,final ReadWriteLockTest rwlt2) {
		for (int i = 0; i < 20; i++) {
			//模拟写客户端
			if((i%2)==0){
//			if(i==0){
				Thread thread = new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							rwlt.write();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				thread.start();
			}else{
				//模拟读客户端
				Thread thread = new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							rwlt2.read();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				thread.start();
			}
		}
	}
	
	/**
     * 读写锁测试对象
     */
    class ReadWriteLockTest {
        // 测试数据变更字段,模拟自己进程内部的数据
        private Integer testData = 0;
        private Set<Thread> threadSet = new HashSet<>();
        
        private boolean reentrantWrite = true;
     // 写入数据
        public void write() throws Exception {
        	final ReadWriteLockTest me = this;
        	writeClientLock.writeLock(new ReadWriteCallback<Void>(30,TimeUnit.SECONDS){
				@Override
				public Void callback() {
					try {
						Thread.sleep(10);
						testData++;
						TestSimpleReadWriteLockListener.common_testData = testData;
						String name = Thread.currentThread().getName();
		                System.out.println("client1_"+name+"写入数据 testData:" + testData+",common_testData:"+TestSimpleReadWriteLockListener.common_testData);
		                
		                if(reentrantWrite){
		                	reentrantWrite = false;
		                	try {
								me.write();
							} catch (Exception e) {
								e.printStackTrace();
							}
		                }
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return null;
				}
        	});
        }
        
        // 读取数据
        private void read() throws Exception {
        	readClientLock.readLock(new ReadWriteCallback<Void>(30,TimeUnit.SECONDS){
				@Override
				public Void callback() {
					try {
						Thread.sleep(10);
						String name = Thread.currentThread().getName();
						 System.out.println("client2_"+name+"读取数据 testData:" + testData+",common_testData:"+TestSimpleReadWriteLockListener.common_testData);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				public void readFail() {
					try {
						Thread.sleep(10);
						Integer b = testData;
						testData = TestSimpleReadWriteLockListener.common_testData;
						String name = Thread.currentThread().getName();
						System.out.println("client2_"+name+"重新加载数据  testData_before:" + b+",common_testData:"+TestSimpleReadWriteLockListener.common_testData+", testData_after:" + testData);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
        	});
        }

    }
    
    private static class RefreshNotifyListener extends RefreshNotify{
		@Override
		public void doRefreshNotify() {
			System.out.println("doRefreshNotify");
		}
    }
}
