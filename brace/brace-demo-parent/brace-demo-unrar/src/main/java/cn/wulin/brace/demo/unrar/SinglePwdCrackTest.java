package cn.wulin.brace.demo.unrar;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;

import cn.wulin.brace.demo.unrar.domain.Crack;
import cn.wulin.brace.demo.unrar.domain.CrackPassword;
import cn.wulin.brace.demo.unrar.helper.CrackPasswordGenerator;
import cn.wulin.brace.demo.unrar.util.CmdUtil;
import cn.wulin.brace.utils.ThreadFactoryImpl;

/**
 * 但密码破解测试类
 * @author ThinkPad
 *
 */
public class SinglePwdCrackTest {
	private final static String PWD_DIC_PATH = System.getProperty("user.dir")+"/config/line-pwd.dic";
	private final static ThreadPoolExecutor executors = new ThreadPoolExecutor(10, 10,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(1000000),new ThreadFactoryImpl("TestRar"));
		
	private static final String SUCCESS_STRING = "Everything is Ok";
	public static void main(String[] args) {
		SinglePwdCrackTest testRar = new SinglePwdCrackTest();
		String[] pwdDic = testRar.getPwdDic();
		CrackPassword crackPassword = new CrackPassword(1, 5);
		CrackPasswordGenerator generator = new CrackPasswordGenerator(crackPassword);
//		Executors.newFixedThreadPool(nThreads)
		
		
		
		
		
		String filePath = "E:\\resources\\download\\xunlei_download\\crackdict\\aaa.rar";
		try {
			
			long start = System.currentTimeMillis();
			
			AtomicLong count = new AtomicLong(0);
			AtomicLong maxCount  = new AtomicLong(0);
			AtomicBoolean isMax = new AtomicBoolean(false);
			while(generator.garentedPwd()) {
				String pwd = crackPassword.getPwdString();
				maxCount.incrementAndGet();
				executors.submit(()->{
					long currentIndex = count.incrementAndGet();
					Crack crack = new Crack(pwd, filePath);
					testRar.tryCrack(crack);
					if(crack.getCrack()) {
						System.out.println("解压密码为: "+crack.getPassword());
						System.out.println(currentIndex+"---"+pwd);
						long end = System.currentTimeMillis();
						System.out.println("共耗时: "+(end-start));
						System.exit(1);
					}else {
						System.out.println(currentIndex+"---"+pwd);
					}
					
					if(isMax.get() && currentIndex >= maxCount.get()) {
						long end = System.currentTimeMillis();
						System.out.println("共耗时: "+(end-start));
						System.exit(1);
					}
					
				});
			}
			isMax.set(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("该字典没有解压密码组合");
	}
	
	public String[] getPwdDic() {
		try {
			List<String> readLines = FileUtils.readLines(new File(PWD_DIC_PATH), Charset.forName("UTF-8"));
			return readLines.toArray(new String[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 尝试破解
	 */
	public void tryCrack(Crack crack) {
		List<String> exec = exec(crack.getFilePath(), crack.getPassword());
		if(exec == null || exec.size() == 0) {
			return;
		}
		
		for (String row : exec) {
			row = row.trim();
			if(SUCCESS_STRING.equals(row)) {
				crack.setCrack(true);
			}
		}
	}
	
	public List<String> exec(String filePath,String password){
		String command = "D:\\software\\generalSoftware\\7-zip\\install\\7z.exe";
		List<String> exec = CmdUtil.exec(command+" t -p"+password+" "+filePath);
		return exec;
	}
}
