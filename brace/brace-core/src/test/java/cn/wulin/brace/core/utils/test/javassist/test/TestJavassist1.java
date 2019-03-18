package cn.wulin.brace.core.utils.test.javassist.test;

import org.junit.Test;

import cn.wulin.brace.core.utils.test.javassist.TccJavassistProxyFactory;
import cn.wulin.brace.core.utils.test.javassist.domain.User;
import cn.wulin.brace.core.utils.test.javassist.service.JavassistService;
import cn.wulin.brace.core.utils.test.javassist.service.UserService;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class TestJavassist1 {
	
	/**
	 * 在执行方法前加一段代码
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 */
	@Test
	public void addCodeInMethod() throws NotFoundException, CannotCompileException{
		addCodeInMethod2();
		
	}
	
	private void addCodeInMethod2() throws NotFoundException, CannotCompileException{
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.get("cn.wulin.brace.core.utils.test.javassist.domain.User");
		CtMethod method = cc.getDeclaredMethod("getId");
//		CtMethod method = cc.getMethod("getId",null);
		
		method.insertAfter("{System.out.println(id+\"-->11\");}");
		cc.toClass();
		
		User user = new User();
		String id = user.getId();
		id = user.getId();
		id = user.getId();
		
		
		System.out.println(id);
	}
	
	@Test
	public void testTccjavassistProxy(){
		TccJavassistProxyFactory factory = new TccJavassistProxyFactory();
		Object proxy = factory.getProxy(new Class[]{UserService.class,JavassistService.class});
		System.out.println(proxy);
	}

}
