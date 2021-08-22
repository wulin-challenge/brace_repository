package cn.wulin.brace.demo.unrar;

import cn.wulin.brace.demo.unrar.domain.Unrar;
import cn.wulin.brace.demo.unrar.domain.UnrarConfigParam;
import cn.wulin.brace.demo.unrar.service.UnrarService;

/**
 * 多密码破解启动类
 * @author wulin
 *
 */
public class MultiPwdCrackStarter {
	private static final String command = "D:\\software\\generalSoftware\\7-zip\\install\\7z.exe";
	
	public static void main(String[] args) {
		String filePath = "E:\\resources\\download\\xunlei_download\\crackdict\\bbb.zip";
		UnrarConfigParam param = new UnrarConfigParam(1, 5);
		param.setCommand(command);
		param.setAddMergeDic(false);
		param.setNativeCache(false);
		
		UnrarService service = new UnrarService(param, filePath);
		
		Unrar unrar = service.startCrack();
		System.out.println(unrar.getCreateText()+":"+unrar.getCrackPwd());
		System.exit(1);
	}

}
