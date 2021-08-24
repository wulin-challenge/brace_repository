package cn.wulin.brace.demo.unrar.test;

import org.junit.Test;

import cn.wulin.brace.demo.unrar.domain.CrackPassword;
import cn.wulin.brace.demo.unrar.helper.CrackPasswordGenerator;

/**
 * 计算密码个数的测试类
 * @author wulin
 *
 */
public class PwdCalculationTest {

	/**
	 * 采用计数法计算的
	 */
	@Test
	public void pwdCalculationCountTest() {
		String[] ps = new String[] {"1","2","3"};
		CrackPassword cp = new CrackPassword(1, 5);
		
		CrackPasswordGenerator cpg = new CrackPasswordGenerator(ps, cp);
		
		int i = 0;
		while(cpg.garentedPwd()) {
			i++;
			System.out.println(i+"---"+cp.getPwdString());
		}
	}
	
	/**
	 * 采用公式法计算的
	 * <p> 原文链接:  http://www.notescloud.top/cloudSearch/detail?id=3164
	 * <pre><code>
	 * n: 表示可用密码的个数,m:表示取多少为密码,c:表示密码的总个数
	 * 
	 * c = n^m+n^(m-1)+n^(m-2)...n^(m-m-1)
	 * </code></pre>
	 */
	@Test
	public void pwdCalculationFormulaTest() {
		double n = 15d;
		double m = 5d;
		
		double count = 0d;
		for (int i = 0; i < m; i++) {
			count += Math.pow(n, m-i);
		}

		System.out.println(count);
	}
}
