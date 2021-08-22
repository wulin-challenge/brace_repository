package cn.wulin.brace.demo.unrar.domain;

public class CrackPassword {
	private String[] pwd = new String[0];
	private String[] lastPwd = new String[0];
	private int minLength;
	private int maxLength;
	
	public CrackPassword(int minLength, int maxLength) {
		super();
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public CrackPassword(String[] pwd, int minLength, int maxLength) {
		super();
		if(pwd != null) {
			this.pwd = pwd;
		}
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public String getPwdString() {
		return getString(pwd);
	}
	
	private String getString(String[] arrays) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < arrays.length; i++) {
			s.append(arrays[i]);
		}
		return s.toString();
	}
	
	public String getLastPwdString() {
		return getString(lastPwd);
	}
	
	public String[] getPwd() {
		return pwd;
	}
	public void setPwd(String[] pwd) {
		this.pwd = pwd;
	}
	public String[] getLastPwd() {
		return lastPwd;
	}
	public void setLastPwd(String[] lastPwd) {
		this.lastPwd = lastPwd;
	}
	public int getMinLength() {
		return minLength;
	}
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
