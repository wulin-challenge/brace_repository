package cn.wulin.brace.demo.unrar.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 组合式密码生成器
 * @author wulin
 *
 */
public class CrackPasswordGenerators {

	/**
	 * 生成密码计数
	 */
	private AtomicLong count = new AtomicLong(0L);
	
	/**
	 * 可用的生成器,不可用的将将会被删除
	 */
	private List<CrackPasswordGenerator> generators = new ArrayList<>();
	
	/**
	 * 当前生成器
	 */
	private CrackPasswordGenerator currentGenerator;
	
	public boolean garentedPwd() {
		if(generators.size() == 0) {
			currentGenerator = null;
			return false;
		}
		
		int index = (int) (count.get()%generators.size());
		CrackPasswordGenerator generator = generators.get(index);
		boolean hasNext = generator.garentedPwd();
		
		if(hasNext) {
			count.incrementAndGet();
			currentGenerator = generator;
			return true;
		}
		generators.remove(index);
		return garentedPwd();
	}
	
	/**
	 * 得到密码
	 * @return
	 */
	public String getPwdString() {
		if(currentGenerator == null) {
			return "";
		}
		return currentGenerator.getCrackPassword().getPwdString();
	}
	
	/**
	 * 得到当前的执行计数
	 * @return
	 */
	public Long getCount() {
		return count.get();
	}

	public List<CrackPasswordGenerator> getGenerators() {
		return generators;
	}

}
