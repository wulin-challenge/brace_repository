package org.wulin.brace.demo.bytes.private_method_field.impl;

import java.net.URL;

import cn.hutool.core.lang.Pair;

public class MyFactory {

	public Pair<String[],URL[]> get(){
		return MyClassLoaderTarget.get();
	}
	
	public void print() {
		new MyClassLoaderTarget().print();
	}
}
