package cn.wulin.brace.demo.unrar.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.wulin.brace.demo.unrar.domain.CrackPassword;

/**
 * 破解密码生成器
 * @author wulin
 *
 */
public class CrackPasswordGenerator {
	/**
	 * 密码字典数组
	 */
	private String[] passwordDictionary = new String[] {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","w","u","v","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","W","U","V","X","Y","Z","!","@","#","$","%","^","&","*","(",")","_","+","<",">","?"};
	
	/**
	 * 密码字典索引
	 */
	private Map<String,Integer> passwordDictionaryIndex = new HashMap<>();
	
	/**
	 * 有下个密码吗?
	 */
	private boolean hasNextPassword = true;
	
	private CrackPassword crackPassword;
	
	public CrackPasswordGenerator(CrackPassword crackPassword) {
		this(null,crackPassword);
	}

	public CrackPasswordGenerator(String[] passwordDictionary,CrackPassword  crackPassword) {
		this.crackPassword = crackPassword;
		if(passwordDictionary != null && passwordDictionary.length>0) {
			this.passwordDictionary = passwordDictionary;
		}
		for (int i = 0; i < passwordDictionary.length; i++) {
			passwordDictionaryIndex.put(passwordDictionary[i], i);
		}
	}

	/**
	 * 生成密码
	 * @param crackPassword
	 * @return
	 */
	public boolean garentedPwd() {
		String[] originPwd = crackPassword.getPwd();
		String[] copyPwd = Arrays.copyOf(originPwd, originPwd.length);
		String[] pwd = garentePwd(copyPwd, crackPassword.getMinLength(), crackPassword.getMaxLength());
		crackPassword.setLastPwd(crackPassword.getPwd());
		crackPassword.setPwd(pwd);
		if(crackPassword.getPwdString().equals(crackPassword.getLastPwdString())) {
			hasNextPassword = false;
			return false;
		}
		return true;
	}

	private String[] garentePwd(String[] pwd,int minLength,int maxLength) {
		if(pwd == null || pwd.length == 0) {
			return getMinString(minLength);
		}
		if(pwd.length <=maxLength) {
			return incrementAndGet(pwd, minLength, maxLength);
		}
		
		return null;
	}
	
	private String[] incrementAndGet(String[] pwd,int minLength,int maxLength) {
		String[] pwdChar = pwd;
		for (int i = pwd.length-1; i > -1; i--) {
			int index = getIndex(pwdChar[i]);
			if(index < passwordDictionary.length-1) {
				String next = passwordDictionary[index+1];
				pwdChar[i] = next;
				return pwdChar;
			}
			if(index == passwordDictionary.length-1 && i != 0) {
				pwdChar[i] = passwordDictionary[0];
				continue;
			}
			if(index == passwordDictionary.length-1 && i == 0) {
				if(pwdChar.length == maxLength) {
					return getMaxString(maxLength);
				}
				if(pwdChar.length<maxLength) {
					return getMinString(pwdChar.length+1);
				}
			}
		}
		return null;
	}
	
	private int getIndex(String c) {
		return passwordDictionaryIndex.get(c);
	}
	
	/**
	 * 得到指定长度的最新值
	 * @param length
	 * @return
	 */
	private String[] getMinString(int length) {
		String[] pwd = new String[length];
		for(int i=0;i<length;i++) {
			
			pwd[i] = passwordDictionary[0];
		}
		return pwd;
	}
	
	/**
	 * 得到指定长度的最大值
	 * @param length
	 * @return
	 */
	private String[] getMaxString(int length) {
		String[] pwd = new String[length];
		for(int i=0;i<length;i++) {
			pwd[i] = passwordDictionary[passwordDictionary.length-1];
		}
		return pwd;
	}

	public boolean hasNextPassword() {
		return hasNextPassword;
	}

	public CrackPassword getCrackPassword() {
		return crackPassword;
	}

}
