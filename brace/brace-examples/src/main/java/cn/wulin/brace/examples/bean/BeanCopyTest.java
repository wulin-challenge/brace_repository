package cn.wulin.brace.examples.bean;

import java.util.Map;

import org.junit.Test;

import cn.hutool.core.bean.BeanUtil;

public class BeanCopyTest {

	@Test
	public void hutoolBeanCopy() {
		
		UserVO userOrigin = new UserVO("1", "张三");
		UserVO userDesc = new UserVO();
		
		BeanUtil.copyProperties(userOrigin, userDesc);
		Map<String, Object> beanToMap = BeanUtil.beanToMap(userOrigin, false, false);
		
		System.out.println();
	}
	
	
	public static class UserVO {
		private String id;
		private String username;
		public UserVO(String id, String username) {
			super();
			this.id = id;
			this.username = username;
		}
		public UserVO() {
			super();
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		
	}
}
