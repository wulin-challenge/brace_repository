package cn.wulin.brace.demo.unrar.helper;

import java.util.List;

import cn.wulin.brace.demo.unrar.domain.Crack;
import cn.wulin.brace.demo.unrar.util.CmdUtil;

/**
 * 解压帮助类
 * @author ThinkPad
 *
 */
public class UnrarHelper {
	
	/**
	 * 成都字符串
	 */
	private String successString = "Everything is Ok";
	private String command = "D:\\software\\generalSoftware\\7-zip\\install\\7z.exe";
	
	public UnrarHelper() {
	}
	
	public UnrarHelper(String command) {
		super();
		this.command = command;
	}

	public UnrarHelper(String successString, String command) {
		super();
		this.successString = successString;
		this.command = command;
	}

	/**
	 * 尝试破解,破解成功返回true,否则false
	 */
	public boolean tryCrack(Crack crack) {
		List<String> exec = exec(crack.getFilePath(), crack.getPassword());
		if(exec == null || exec.size() == 0) {
			return false;
		}
		
		for (String row : exec) {
			row = row.trim();
			if(successString.equals(row)) {
				crack.setCrack(true);
				return true;
			}
		}
		return false;
	}
	
	public List<String> exec(String filePath,String password){
		List<String> exec = CmdUtil.exec(command+" t -p"+password+" "+filePath);
		return exec;
	}

	public String getSuccessString() {
		return successString;
	}

	public void setSuccessString(String successString) {
		this.successString = successString;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
