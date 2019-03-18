package cn.wulin.brace.core.utils.test.javassist.domain;

public class User {
	
	private String id="123";

	public String getId() {
		System.out.println("5555");
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
