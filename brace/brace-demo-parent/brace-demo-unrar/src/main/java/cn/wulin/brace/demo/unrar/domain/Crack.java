package cn.wulin.brace.demo.unrar.domain;

public class Crack {
	/**
	 * 破解密码
	 */
	private String password;
	
	/**
	 * 破解是否成功
	 */
	private Boolean crack = false;
	
	private String filePath;
	
	public Crack(String password, String filePath) {
		super();
		this.password = password;
		this.filePath = filePath;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getCrack() {
		return crack;
	}

	public void setCrack(Boolean crack) {
		this.crack = crack;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
