package cn.wulin.brace.demo.multi.extendz.test;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import cn.wulin.brace.demo.multi.extendz.PersonParentClass;
import cn.wulin.brace.demo.multi.extendz.PersonParentInterface;
import cn.wulin.brace.demo.multi.extendz.impl.UserImpl;

/**
 * 测试使用接口模拟与抽象类相同功能的多继承
 * @author wulin
 *
 */
public class TestMultiExtends {
	
	public static void main(String[] args) {
		new TestMultiExtends().testGCReference();
	}

	/**
	 * 测试gc应用
	 */
	@Test
	public void testGCReference() {
		strongReference();
		noStrongReference();
		System.out.println();
	}
	
	/**
	 * 无强应用
	 */
	private void noStrongReference() {
		Set<PersonParentInterface> keySet = PersonParentInterface.reference.keySet();
		
		//手动执行gc
		Collection<PersonParentClass> values = PersonParentInterface.reference.values();
		
		for (PersonParentInterface user : keySet) {
			PersonParentClass personParentClass = PersonParentInterface.reference.get(user);
			System.out.println(personParentClass.getNickname());
			System.out.println(personParentClass.getSex());
			System.out.println();
		}
	}
	
	/**
	 * 据用强引用的情况
	 */
	private void strongReference() {
		//强应用
		UserImpl user = getUser();
		System.out.println(user.getUsername());
		System.out.println(user.getNickname());
		System.out.println();
		
		PersonParentClass personParentClass = PersonParentInterface.reference.get(user);
		System.out.println(personParentClass.getNickname());
		System.out.println(personParentClass.getSex());
		System.out.println();
	}
	
	private UserImpl getUser() {
		UserImpl user = new UserImpl();
		user.setUsername("zhangsan");
		user.setNickname("张三");
		user.setPassword("123456789");
		user.setSex(1);
		user.setHeight(185);
		
		return user;
	}
}
