package cn.wulin.brace.demo.unrar;

import java.util.Set;

import org.wulin.basic.util.PinYinUtil;

public class PinyinTest {

	public static void main(String[] args) throws Exception {
		
		String ch = "一站式学习a网络编程 全面理解";
		Set<String> hanyuToPy = PinYinUtil.hanyuToPy(ch," ");
		for (String string : hanyuToPy) {
			System.out.println(string);
		}
		
	}
}
