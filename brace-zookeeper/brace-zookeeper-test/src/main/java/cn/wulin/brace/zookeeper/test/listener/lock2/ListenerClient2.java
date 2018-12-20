package cn.wulin.brace.zookeeper.test.listener.lock2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.wulin.brace.zookeeper.locks.ReadWriteCallback;
import cn.wulin.brace.zookeeper.locks.RefreshNotify;
import cn.wulin.brace.zookeeper.locks.SharedReentrantReadWriteLock;
import cn.wulin.brace.zookeeper.test.listener.lock2.ListenerCommonDB.User;

/**
 * 监听客户端2
 * @author wubo
 *
 */
public class ListenerClient2 {
	
	private List<User> userList = new ArrayList<User>();
	
	public static final String lock = "/locks/listener";
	
	private RefreshNotify listener = new RefreshNotifyListener(this);
	//客户端1
	private SharedReentrantReadWriteLock clientLock = new SharedReentrantReadWriteLock(lock,listener);
	
	private static class RefreshNotifyListener extends RefreshNotify{
		private ListenerClient2 listenerClient2;
		
		public RefreshNotifyListener(ListenerClient2 listenerClient1) {
			super();
			this.listenerClient2 = listenerClient1;
		}

		@Override
		public void doRefreshNotify() {
			System.out.println("client2-->doRefreshNotify-->刷新...");
			listenerClient2.refresh();
		}
    }
	
	public void multiThread(){
		multiThreadWrite();
		multiThreadRead();
	}
	
	private void read(int i){
		System.out.println("read---->"+i);
		synchronized (ListenerClient2.class) {
			System.out.println(userList);
		}
	}
	
	private void write(int i){
		System.out.println("write---->"+i);
		User user = new User(i+"", "client2_"+i, i+"", new Date());
		ListenerCommonDB.saveUser(user);
		
		clientLock.writeLock(new ReadWriteCallback<Void>(3000,TimeUnit.SECONDS){
			@Override
			public Void callback() {
				return null;
			}
		});
	}
	
	private void multiThreadRead(){
		Thread thread = new Thread(new Runnable(){
			public void run() {
				for (int i = 0; i < 10; i++) {
					final int j = i;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Thread read = new Thread(new Runnable(){
						public void run() {
							read(j);
						}
					});
					read.start();
				}
			}
		});
		thread.start();
	}
	
	private void multiThreadWrite(){
		Thread thread = new Thread(new Runnable(){
			public void run() {
				for (int i = 0; i < 10; i++) {
					final int j = i;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Thread write = new Thread(new Runnable(){
						public void run() {
							write(j);
						}
					});
					write.start();
				}
			}
		});
		thread.start();
	}
	
	private void refresh(){
		List<User> findAllUser = ListenerCommonDB.findAllUser();
		if(findAllUser != null){
			synchronized (ListenerClient2.class) {
				userList.clear();
				for (User user : findAllUser) {
					userList.add(user);
				}
			}
		}
	}
}
