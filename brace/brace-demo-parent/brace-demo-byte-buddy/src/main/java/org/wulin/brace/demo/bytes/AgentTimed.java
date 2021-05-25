package org.wulin.brace.demo.bytes;

import cn.wulin.ioc.extension.Activate;

@Activate
public class AgentTimed {
	
	public String execute(String hello) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(hello + " word");
		return "测试";
	}

}
