package cn.wulin.brace.examples.spring.cglib;

import java.util.Map;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

/**
 * cglib工具类
 * @author wulin
 *
 */
public class CglibBeanUtil {
	/**
	 * 实体Object
	 */
	public Object object = null;

	/**
	 * 属性map
	 */
	public BeanMap beanMap = null;

	/**
	 * 创建一个空的cglib对象
	 * @param propertyMap key:cglib的属性,value,key属性对应的类型
	 */
	public CglibBeanUtil(Map<String, Class<?>> propertyMap) {
		this.object = generateCglibBean(propertyMap);
		this.beanMap = BeanMap.create(this.object);
	}
	
	/**
	 * 给bean属性赋值
	 * 
	 * @param property
	 *            属性名
	 * @param value
	 *            值
	 */
	public void setValue(String property, Object value) {
		beanMap.put(property, value);
	}

	/**
	 * 通过属性名得到属性值
	 * 
	 * @param property
	 *            属性名
	 * @return 值
	 */
	public Object getValue(String property) {
		return beanMap.get(property);
	}

	/**
	 * 得到该实体bean对象
	 * 
	 * @return
	 */
	public Object getObject() {
		return this.object;
	}

	/**
	 * 生成空的cglibBean
	 * @param propertyMap key:cglib的属性,value,key属性对应的类型
	 * @return 空 cglib bean
	 */
	private Object generateCglibBean(Map<String, Class<?>> propertyMap) {
		BeanGenerator generator = new BeanGenerator();
		for (String key : propertyMap.keySet()) {
			generator.addProperty(key, propertyMap.get(key));
		}
		return generator.create();
	}
	
	/**
	 * 将cglib转为Map
	 * @param cglibBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Object,Object> cglibBeanConvertMap(Object cglibBean){
		return BeanMap.create(cglibBean);
	}
	
	/**
	 * 将Map转为为Bean
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static <T> T mapToBean(Class<T> clazz,Map<Object,Object> map){
		T bean = null;
		try {
			bean = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		BeanMap beanMap = BeanMap.create(bean);
		beanMap.putAll(map);
		return bean;
	}
}
