package cn.wulin.brace.demo.unrar;

import java.util.Set;

import org.wulin.basic.util.PinYinUtil;

public class PinyinTest {

	public static void main(String[] args) throws Exception {
		
		String ch = "新a精讲，项目驱动落地，分布式事务拔高";
		Set<String> hanyuToPy = PinYinUtil.hanyuToPy(ch," ");
		for (String string : hanyuToPy) {
			System.out.println(string);
		}
		
	}
}
