package cn.wulin.brace.core.utils.test.serialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Param /*implements Serializable*/{
//	private static final long serialVersionUID = 1L;
	private Object[] params = new Object[]{"123",null,321};
	private List<Object> p = new ArrayList<>();
	
	public Param(){
		for (Object object : params) {
			p.add(object);
		}
	}
	
	public Object[] getParams() {
		return params;
	}

	public List<Object> getP() {
		return p;
	}
	
	
}