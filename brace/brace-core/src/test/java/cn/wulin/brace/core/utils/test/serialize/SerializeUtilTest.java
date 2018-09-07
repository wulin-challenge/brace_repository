package cn.wulin.brace.core.utils.test.serialize;

import org.junit.Test;

import cn.wulin.brace.core.utils.SerializeUtil;

public class SerializeUtilTest {
	
	@Test
	public void serialize(){
		Param param = new Param();
//		byte[] data = SerializeUtil.serialize(param);
		byte[] data = SerializeUtil.serialize(param,"kryo");
		
//		Param param2 = SerializeUtil.deserialize(data, Param.class);
		Param param2 = SerializeUtil.deserialize(data, Param.class,"kryo");
		System.out.println(param2);
	}
	
	@Test
	public void serializeBaseType(){
		String s = "哈哈123";
		byte[] data = SerializeUtil.serialize(s);
		
		String param2 = SerializeUtil.deserialize(data, String.class);
		System.out.println(param2);
	}
	
	@Test
	public void KryoUtilTest(){
		Param param = new Param();
		byte[] data = SerializeUtil.serialize(param,"kryo");
		Param param2 = SerializeUtil.deserialize(data, Param.class,"kryo");
		System.out.println(param2);
	}

}
