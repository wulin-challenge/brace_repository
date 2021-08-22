package cn.wulin.brace.demo.unrar.domain;

/**
 * 破解的结果
 * @author wulin
 *
 */
public class Unrar {

	/**
	 * 破解成功吗?
	 */
	private boolean crackSuccess = false;
	
	/**
	 * 破解密码
	 */
	private String crackPwd;
	
	/**
	 * 破解成功或者失败的提示文本
	 */
	private String createText = "";
	
	/**
	 * 开始时间
	 */
	private Long startTime = 0L;
	
	/**
	 * 结束时间
	 */
	private Long endTime = 0L;
	
	public Unrar() {
		super();
	}

	public Unrar(boolean crackSuccess, String crackPwd) {
		super();
		this.crackSuccess = crackSuccess;
		this.crackPwd = crackPwd;
	}

	public Unrar(boolean crackSuccess, String crackPwd, String createText) {
		super();
		this.crackSuccess = crackSuccess;
		this.crackPwd = crackPwd;
		this.createText = createText;
	}

	public boolean getCrackSuccess() {
		return crackSuccess;
	}

	public void setCrackSuccess(boolean crackSuccess) {
		this.crackSuccess = crackSuccess;
	}

	public String getCrackPwd() {
		return crackPwd;
	}

	public void setCrackPwd(String crackPwd) {
		this.crackPwd = crackPwd;
	}

	public String getCreateText() {
		return createText;
	}

	public void setCreateText(String createText) {
		this.createText = createText;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Unrar [crackSuccess=" + crackSuccess + ", crackPwd=" + crackPwd + ", createText=" + createText
				+ ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	
}
