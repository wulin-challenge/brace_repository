package cn.wulin.brace.examples.spring.cglib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wulin.brace.core.utils.SerializeUtil;

/**
 * 将spring的cglib 的bean转换为Map和Map转为bean的案例
 * @author ThinkPad
 *
 */
public class CglibBeabToMap {
	
	public static void main(String[] args) {
		CglibBeabToMap cm = new CglibBeabToMap();
		cm.cglibBeanToMap(cm.getCglibBeanList());
	}
	
	/**
	 * 得到cglib bean
	 * @return
	 */
	private List<Object> getCglibBeanList(){
		Map<String,Class<?>> propertyType = new HashMap<String,Class<?>>();
		propertyType.put("id", String.class);
		propertyType.put("age", Integer.class);
		
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < 5; i++) {
			CglibBeanUtil cglibBeanUtil = new CglibBeanUtil(propertyType);
			cglibBeanUtil.setValue("id", i+"");
			cglibBeanUtil.setValue("age", i*10);
			list.add(cglibBeanUtil.getObject());
		}
		return list;
	}
	
	/**
	 * 将cglibBean转为Map
	 * @param cglibBeanList
	 */
	private void cglibBeanToMap(List<Object> cglibBeanList){
		for (Object cglibBean : cglibBeanList) {
			Map<Object,Object> map = CglibBeanUtil.cglibBeanConvertMap(cglibBean);
			User user = CglibBeanUtil.mapToBean(User.class, map);
			
			//测试序列化
			byte[] data = SerializeUtil.serialize(user,"jdk");
			User user2 = SerializeUtil.deserialize(data, User.class, "jdk");
			
			System.out.println("{id:"+map.get("id")+",age:"+map.get("age")+"}");
		}
	}
	
	public static class User implements java.io.Serializable{
		private String id;
		private Integer age;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
	}

}
