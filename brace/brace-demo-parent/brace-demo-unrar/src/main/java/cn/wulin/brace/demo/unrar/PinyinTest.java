package cn.wulin.brace.demo.unrar;

import java.util.ArrayList;

import org.wulin.basic.util.PinYinUtil;

public class PinyinTest {

//	public static void main(String[] args) throws Exception {
//		
//		String ch = "开发工程师";
//		Set<String> hanyuToPy = PinYinUtil.hanyuToPy(ch," ");
//		for (String string : hanyuToPy) {
//			System.out.println("-- "+string);
//		}
//	}
	
	public static void main(String[] args) throws Exception {
//		
		String[] ch = getString().split("\\r\\n");
		for (int i = 0; i < ch.length; i++) {
			String string = new ArrayList<>(PinYinUtil.hanyuToPy(ch[i])).get(0);
			
			System.out.println(string);
		}
		
	}
	
	private static String getString() {
		return "嘉懿\r\n" + 
				"煜城\r\n" + 
				"懿轩\r\n" + 
				"烨伟\r\n" + 
				"苑博\r\n" + 
				"伟泽\r\n" + 
				"熠彤\r\n" + 
				"鸿煊\r\n" + 
				"博涛\r\n" + 
				"烨霖\r\n" + 
				"烨华\r\n" + 
				"煜祺\r\n" + 
				"智宸\r\n" + 
				"正豪\r\n" + 
				"昊然\r\n" + 
				"明杰\r\n" + 
				"立诚\r\n" + 
				"立轩\r\n" + 
				"立辉\r\n" + 
				"峻熙\r\n" + 
				"弘文\r\n" + 
				"熠彤\r\n" + 
				"鸿煊\r\n" + 
				"烨霖\r\n" + 
				"哲瀚\r\n" + 
				"鑫鹏\r\n" + 
				"致远\r\n" + 
				"俊驰\r\n" + 
				"雨泽\r\n" + 
				"烨磊\r\n" + 
				"晟睿\r\n" + 
				"天佑\r\n" + 
				"文昊\r\n" + 
				"修洁\r\n" + 
				"黎昕\r\n" + 
				"远航\r\n" + 
				"旭尧\r\n" + 
				"鸿涛\r\n" + 
				"伟祺\r\n" + 
				"荣轩\r\n" + 
				"越泽\r\n" + 
				"浩宇\r\n" + 
				"瑾瑜\r\n" + 
				"皓轩\r\n" + 
				"擎苍\r\n" + 
				"擎宇\r\n" + 
				"志泽\r\n" + 
				"睿渊\r\n" + 
				"楷瑞\r\n" + 
				"子轩\r\n" + 
				"弘文\r\n" + 
				"哲瀚\r\n" + 
				"雨泽\r\n" + 
				"鑫磊\r\n" + 
				"修杰\r\n" + 
				"伟诚\r\n" + 
				"建辉\r\n" + 
				"晋鹏\r\n" + 
				"天磊\r\n" + 
				"绍辉\r\n" + 
				"泽洋\r\n" + 
				"明轩\r\n" + 
				"健柏\r\n" + 
				"鹏煊\r\n" + 
				"昊强\r\n" + 
				"伟宸\r\n" + 
				"博超\r\n" + 
				"君浩\r\n" + 
				"子骞\r\n" + 
				"明辉\r\n" + 
				"鹏涛\r\n" + 
				"炎彬\r\n" + 
				"鹤轩\r\n" + 
				"越彬\r\n" + 
				"风华\r\n" + 
				"靖琪\r\n" + 
				"明诚\r\n" + 
				"高格\r\n" + 
				"光华\r\n" + 
				"国源\r\n" + 
				"冠宇\r\n" + 
				"晗昱\r\n" + 
				"涵润\r\n" + 
				"翰飞\r\n" + 
				"翰海\r\n" + 
				"昊乾\r\n" + 
				"浩博\r\n" + 
				"和安\r\n" + 
				"弘博\r\n" + 
				"宏恺\r\n" + 
				"鸿朗\r\n" + 
				"华奥\r\n" + 
				"华灿\r\n" + 
				"嘉慕\r\n" + 
				"坚秉\r\n" + 
				"建明\r\n" + 
				"金鑫\r\n" + 
				"锦程\r\n" + 
				"瑾瑜\r\n" + 
				"晋鹏\r\n" + 
				"经赋\r\n" + 
				"景同\r\n" + 
				"靖琪\r\n" + 
				"君昊\r\n" + 
				"俊明\r\n" + 
				"季同\r\n" + 
				"开济\r\n" + 
				"凯安\r\n" + 
				"康成\r\n" + 
				"乐语\r\n" + 
				"力勤\r\n" + 
				"良哲\r\n" + 
				"理群\r\n" + 
				"茂彦\r\n" + 
				"敏博\r\n" + 
				"明达\r\n" + 
				"朋义\r\n" + 
				"彭泽\r\n" + 
				"鹏举\r\n" + 
				"濮存\r\n" + 
				"溥心\r\n" + 
				"璞瑜\r\n" + 
				"浦泽\r\n" + 
				"奇邃\r\n" + 
				"祺祥\r\n" + 
				"荣轩\r\n" + 
				"锐达\r\n" + 
				"睿慈\r\n" + 
				"绍祺\r\n" + 
				"圣杰\r\n" + 
				"晟睿\r\n" + 
				"思源\r\n" + 
				"斯年\r\n" + 
				"泰宁\r\n" + 
				"天佑\r\n" + 
				"同巍\r\n" + 
				"奕伟\r\n" + 
				"祺温\r\n" + 
				"文虹\r\n" + 
				"向笛\r\n" + 
				"心远\r\n" + 
				"欣德\r\n" + 
				"新翰\r\n" + 
				"兴言\r\n" + 
				"星阑\r\n" + 
				"修为\r\n" + 
				"旭尧\r\n" + 
				"炫明\r\n" + 
				"学真\r\n" + 
				"雪风\r\n" + 
				"雅昶\r\n" + 
				"阳曦\r\n" + 
				"烨熠\r\n" + 
				"英韶\r\n" + 
				"永贞\r\n" + 
				"咏德\r\n" + 
				"宇寰\r\n" + 
				"雨泽\r\n" + 
				"玉韵\r\n" + 
				"越彬\r\n" + 
				"蕴和\r\n" + 
				"哲彦\r\n" + 
				"振海\r\n" + 
				"正志\r\n" + 
				"子晋\r\n" + 
				"自怡\r\n" + 
				"德赫\r\n" + 
				"君平\r\n" + 
				"伟\r\n" + 
				"芳\r\n" + 
				"娜\r\n" + 
				"秀英\r\n" + 
				"刘伟\r\n" + 
				"敏\r\n" + 
				"静\r\n" + 
				"丽\r\n" + 
				"静\r\n" + 
				"丽\r\n" + 
				"强\r\n" + 
				"静\r\n" + 
				"敏\r\n" + 
				"敏\r\n" + 
				"磊\r\n" + 
				"军\r\n" + 
				"洋\r\n" + 
				"勇\r\n" + 
				"勇\r\n" + 
				"艳\r\n" + 
				"杰\r\n" + 
				"磊\r\n" + 
				"强\r\n" + 
				"军\r\n" + 
				"杰\r\n" + 
				"娟\r\n" + 
				"艳\r\n" + 
				"涛\r\n" + 
				"涛\r\n" + 
				"明\r\n" + 
				"艳\r\n" + 
				"超\r\n" + 
				"勇\r\n" + 
				"娟\r\n" + 
				"杰\r\n" + 
				"秀兰\r\n" + 
				"霞\r\n" + 
				"敏\r\n" + 
				"军\r\n" + 
				"丽\r\n" + 
				"强\r\n" + 
				"平\r\n" + 
				"刚\r\n" + 
				"杰\r\n" + 
				"桂英\r\n" + 
				"波\r\n" + 
				"均";
	}
}
