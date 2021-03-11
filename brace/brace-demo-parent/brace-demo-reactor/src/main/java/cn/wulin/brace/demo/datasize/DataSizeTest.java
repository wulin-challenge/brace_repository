package cn.wulin.brace.demo.datasize;

import org.junit.Test;

public class DataSizeTest {
	
	private static class ObjectA {  
        String str;  // 4  

		public ObjectA(String str) {
			super();
			this.str = str;
		}
        
        
    }  

    private static class ObjectB {  

    }  

    @Test
	public void dataSize() {
		final ClassIntrospector ci = new ClassIntrospector();  

        ObjectInfo res;  
        
        try {
        	ObjectA a = new ObjectA("疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据疯狂京东数科范德萨范德萨连接符的数据");
			res = ci.introspect(a);
			System.out.println( res.getDeepSize() );  
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}  
        
	}
}
