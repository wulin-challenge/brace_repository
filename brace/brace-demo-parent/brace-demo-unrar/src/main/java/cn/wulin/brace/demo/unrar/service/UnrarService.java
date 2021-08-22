package cn.wulin.brace.demo.unrar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wulin.brace.demo.unrar.dao.UnrarRepository;
import cn.wulin.brace.demo.unrar.domain.Crack;
import cn.wulin.brace.demo.unrar.domain.CrackPassword;
import cn.wulin.brace.demo.unrar.domain.Unrar;
import cn.wulin.brace.demo.unrar.domain.UnrarConfigParam;
import cn.wulin.brace.demo.unrar.exception.BlockQueueExecutionHandler;
import cn.wulin.brace.demo.unrar.helper.CrackPasswordGenerator;
import cn.wulin.brace.demo.unrar.helper.CrackPasswordGenerators;
import cn.wulin.brace.demo.unrar.helper.UnrarHelper;
import cn.wulin.brace.demo.unrar.util.ReadDicUtil;
import cn.wulin.brace.utils.ThreadFactoryImpl;

/**
 * 破击服务类
 * @author wulin
 *
 */
public class UnrarService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UnrarService.class);
	private final static ThreadPoolExecutor EXECUTORS = new ThreadPoolExecutor(10, 10,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(1000000),new ThreadFactoryImpl("UnrarService"),new BlockQueueExecutionHandler());
	
	/**
	 * 破解密码的参数配置
	 */
	private UnrarConfigParam unrarConfigParam;
	
	/**
	 * 要破解的文件路径
	 */
	private String toCrackFilePath;
	
	/**
	 * 组合式密码生成器
	 */
	private CrackPasswordGenerators generators = new CrackPasswordGenerators();
	
	private UnrarHelper unrarHelper;
	
	/**
	 * 线程计数器
	 */
	private CountDownLatch threadCount = new CountDownLatch(2);
	
	/**
	 * 是否执行成功
	 */
	private volatile boolean executeSuccess = false;
	
	/**
	 * 破解结果
	 */
	private volatile Unrar crackResult = new Unrar();
	
	/**
	 * 生产者任务添加完成了吗?
	 */
	private volatile boolean productComplete = false;
	
	/**
	 * 消费者任务
	 */
	private volatile AtomicLong consumerTaskCount = new AtomicLong(0L);
	
	public UnrarService(UnrarConfigParam unrarConfigParam,String toCrackFilePath) {
		super();
		this.unrarConfigParam = unrarConfigParam;
		this.toCrackFilePath = toCrackFilePath;
		this.unrarHelper = new UnrarHelper(unrarConfigParam.getCommand());
		generators.getGenerators().addAll(getPwdGenerators());
		
		//从本地初始化到内存
		if(unrarConfigParam.getNativeCache()) {
			UnrarRepository.getInstance().initCacheFromLocal(toCrackFilePath);
		}
		
	}
	
	/**
	 * 开始破解
	 */
	public Unrar startCrack() {
		startProductThread();
		return crackResult;
	}
	
	/**
	 * 开始启动生产者线程
	 */
	private void startProductThread() {
		
		ThreadFactoryImpl threadFactory = new ThreadFactoryImpl("UnrarProduct");
		Thread productThread = threadFactory.newThread(()-> {
			while(generators.garentedPwd()) {
				if(executeSuccess) {
					threadCount.countDown();
					break;
				}
				//从内存中判断是否已经存在
				String pwdString = generators.getPwdString();
				
				if(unrarConfigParam.getNativeCache()) {
					boolean flag = UnrarRepository.getInstance().contains(pwdString);
					if(flag) {
						continue;
					}
				}
				
				submitTask();
			}
			productComplete = true;
			threadCount.countDown();
		});
		crackResult.setStartTime(System.currentTimeMillis());
		productThread.start();
		
		try {
			threadCount.await();
		} catch (InterruptedException e) {
			LOGGER.error("生产者线程等候失败!",e);
		}
	}
	
	private void submitTask() {
		String pwdString = generators.getPwdString();
		EXECUTORS.submit(()->{
			if(executeSuccess) {
				return;
			}
			Crack crack = new Crack(pwdString, toCrackFilePath);
			if(unrarHelper.tryCrack(crack)) {
				crackResult.setCrackSuccess(true);
				crackResult.setCrackPwd(crack.getPassword());
				crackResult.setCreateText("密码破解成功");
				crackResult.setEndTime(System.currentTimeMillis());
				executeSuccess = true;
				
				print(crack, consumerTaskCount.incrementAndGet());;
				threadCount.countDown();
				return;
			}
			//写缓存
			if(unrarConfigParam.getNativeCache()) {
				UnrarRepository.getInstance().saveOrUpdate(crack,true,true);
			}
			
			long taskCount = consumerTaskCount.incrementAndGet();
			//判断是否为最后的消费任务
			if(productComplete && taskCount == generators.getCount()) {
				crackResult.setEndTime(System.currentTimeMillis());
				crackResult.setCreateText("本次破解已完成,但没有获取到破解密码!");
				
				print(crack, consumerTaskCount.get());;
				threadCount.countDown();
			}else {
				print(crack, consumerTaskCount.get());;
			}
		});
	}
	
	/**
	 * 打印破解过程
	 * @param crack
	 * @param consumerCount
	 */
	private void print(Crack crack,Long consumerCount) {
		if(!unrarConfigParam.getPrint()) {
			return;
		}
		Long startTime = crackResult.getStartTime();
		long endTime = System.currentTimeMillis();
		
		System.out.println((endTime-startTime)+"<--->"+consumerCount+"<-->"+crack.getPassword());
		if(crack.getCrack()) {
			System.out.println("成功破解,解压密码为:"+crack.getPassword());
		}
	}
	
	/**
	 * 得到密码生成器
	 * @return
	 */
	private List<CrackPasswordGenerator> getPwdGenerators() {
		List<CrackPasswordGenerator> generatorList = new ArrayList<>();
		List<String[]> readPwdDicList = ReadDicUtil.readPwdDicList(unrarConfigParam.getAddMergeDic());
		for (String[] pwdDic : readPwdDicList) {
			
			//若果实际字典长度小于指定最大长度,则使用字典的实际长度作为最大长度
			int maxLength = unrarConfigParam.getMaxLength();
			if(pwdDic.length < unrarConfigParam.getMaxLength()) {
				maxLength = pwdDic.length;
			}
			
			CrackPassword newCrack = new CrackPassword(unrarConfigParam.getPwd(),unrarConfigParam.getMinLength(), maxLength);
			CrackPasswordGenerator generator = new CrackPasswordGenerator(pwdDic,newCrack);
			generatorList.add(generator);
		}
		return generatorList;
	}
}
