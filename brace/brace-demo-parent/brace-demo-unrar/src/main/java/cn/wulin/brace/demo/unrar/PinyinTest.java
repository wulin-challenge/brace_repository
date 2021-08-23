package cn.wulin.brace.demo.unrar;

import java.util.Set;

import org.wulin.basic.util.PinYinUtil;

public class PinyinTest {

	public static void main(String[] args) throws Exception {
		
		String ch = "用 a 造轮子 全栈开发旅游电商应用";
		Set<String> hanyuToPy = PinYinUtil.hanyuToPy(ch," ");
		for (String string : hanyuToPy) {
			System.out.println(string);
		}
	}
}
