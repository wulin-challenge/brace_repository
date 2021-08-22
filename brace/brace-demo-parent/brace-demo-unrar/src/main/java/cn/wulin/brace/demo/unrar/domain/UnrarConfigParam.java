package cn.wulin.brace.demo.unrar.domain;

/**
 * Unrar破解参数
 * @author wulin
 *
 */
public class UnrarConfigParam {
	private String[] pwd = new String[0];
	private int minLength;
	private int maxLength;
	private String command = "D:\\software\\generalSoftware\\7-zip\\install\\7z.exe";
	
	/**
	 * 是否打印执行过程
	 */
	private Boolean print = true;
	
	/**
	 * 添加合并字典
	 */
	private Boolean addMergeDic = true;
	
	/**
	 * 是否启用本地缓存
	 */
	private Boolean nativeCache = true;
	
	public UnrarConfigParam(int minLength, int maxLength) {
		super();
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public UnrarConfigParam(String[] pwd, int minLength, int maxLength) {
		super();
		this.pwd = pwd;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public String[] getPwd() {
		return pwd;
	}

	public void setPwd(String[] pwd) {
		this.pwd = pwd;
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

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Boolean getPrint() {
		return print;
	}

	public void setPrint(Boolean print) {
		this.print = print;
	}

	public Boolean getAddMergeDic() {
		return addMergeDic;
	}

	public void setAddMergeDic(Boolean addMergeDic) {
		this.addMergeDic = addMergeDic;
	}

	public Boolean getNativeCache() {
		return nativeCache;
	}

	public void setNativeCache(Boolean nativeCache) {
		this.nativeCache = nativeCache;
	}
	
}
