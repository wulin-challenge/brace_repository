package cn.wulin.brace.demo.unrar.util;

import java.util.concurrent.atomic.AtomicInteger;

public class PwdUtil {
//	private static final String CRACK_STRING = "0123456789abcdefghijklmnopqrstwuvxyzABCDEFGHIJKLMNOPQRSTWUVXYZ!@#$%^&*()_+<>?";
	private static final char[] CRACK_CHAR = new char[] {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','w','u','v','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','W','U','V','X','Y','Z','!','@','#','$','%','^','&','*','(',')','_','+','<','>','?'};

	public static void main(String[] args) {
//		String s = "";
//		for (int i = 0; i < CRACK_STRING.length(); i++) {
//			s +=",'"+CRACK_STRING.charAt(i)+"'";
//		}
//		System.out.println(s);
		Long i = 0L;
		long start = System.currentTimeMillis();
		String pwd = "";
		String nextPwd = "";
		while(!pwd.equals((nextPwd=garentePwd(pwd, 3, 4)))) {
			i++;
			pwd = nextPwd;
			System.out.println(i+"---"+pwd);
		}
		long end = System.currentTimeMillis();
		System.out.println("time: "+(end-start));
	}

	public static String garentePwd(String pwd,int minLength,int maxLength) {
		if(pwd.length() == 0) {
			return getMinString(minLength);
		}
		if(pwd.length() <=maxLength) {
			return incrementAndGet(pwd, minLength, maxLength);
		}
		
		return null;
	}
	
	private static String incrementAndGet(String pwd,int minLength,int maxLength) {
		char[] pwdChar = pwd.toCharArray();
		for (int i = pwd.length()-1; i > -1; i--) {
			int index = getIndex(pwdChar[i]);
			if(index < CRACK_CHAR.length-1) {
				char next = CRACK_CHAR[index+1];
				pwdChar[i] = next;
				return new String(pwdChar);
			}
			if(index == CRACK_CHAR.length-1 && i != 0) {
				pwdChar[i] = CRACK_CHAR[0];
				continue;
			}
			if(index == CRACK_CHAR.length-1 && i == 0) {
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
	
	private static int getIndex(char c) {
		for (int i = 0; i < CRACK_CHAR.length; i++) {
			if(c == CRACK_CHAR[i]) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 得到指定长度的最新值
	 * @param length
	 * @return
	 */
	private static String getMinString(int length) {
		StringBuilder pwd = new StringBuilder();
		for(int i=0;i<length;i++) {
			pwd.append(CRACK_CHAR[0]);
		}
		return pwd.toString();
	}
	
	/**
	 * 得到指定长度的最大值
	 * @param length
	 * @return
	 */
	private static String getMaxString(int length) {
		StringBuilder pwd = new StringBuilder();
		for(int i=0;i<length;i++) {
			pwd.append(CRACK_CHAR[CRACK_CHAR.length-1]);
		}
		return pwd.toString();
	}
}
