package cn.wulin.brace.demo.multi.extendz.impl;

import cn.wulin.brace.demo.multi.extendz.AbstractUsername;
import cn.wulin.brace.demo.multi.extendz.PersonParentInterface;

public class UserImpl extends AbstractUsername implements PersonParentInterface {

	public UserImpl() {
		super();
		PersonParentInterface.super.constructionMethod();
	}
	
}
