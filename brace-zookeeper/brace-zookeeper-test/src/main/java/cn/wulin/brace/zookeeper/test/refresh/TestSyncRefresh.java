package cn.wulin.brace.zookeeper.test.refresh;

import java.io.IOException;

import cn.wulin.brace.zookeeper.locks.refresh.RefrechHook;
import cn.wulin.brace.zookeeper.locks.refresh.SyncWriteLockRefreshStrategy;
import cn.wulin.brace.zookeeper.locks.refresh.WriteLockRefreshStrategy;

public class TestSyncRefresh {
	
	WriteLockRefreshStrategy syncRefresh = new SyncWriteLockRefreshStrategy();
	
	
	public static void main(String[] args) throws IOException {
		TestSyncRefresh testSyncRefresh = new TestSyncRefresh();
		testSyncRefresh.mutilThreadtest();
		System.in.read();
	}
	
	private void mutilThreadtest(){
		for (int i = 0; i < 4; i++) {
			final int j = i;
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
					if(j ==1){
						sleep(3000);
//						sleep(1000);
					}
					if(j ==2){
						sleep(15000);
					}
					if(j ==3){
						sleep(18000);
					}
//					if(j ==4){
//						sleep(15000);
//						mutilThreadtest2();
//					}
					syncRefreshTest1();
				}

				private void sleep(long time) {
					try {
						Thread.sleep(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
		
	}
	
	private void mutilThreadtest2(){
		for (int i = 0; i < 100; i++) {
			final int j = i;
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
					if(j ==1){
						sleep(1000);
					}
					if(j ==4){
						sleep(15000);
					}
					syncRefreshTest1();
				}

				private void sleep(long time) {
					try {
						Thread.sleep(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
		
	}
	
	
	private void syncRefreshTest1(){
		syncRefresh.refresh(new RefrechHook(){

			@Override
			public void doRefresh() {
				System.out.println("正在重新刷新数据......");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("数据已经刷新完毕!");
			}
			
		});
	}

}
