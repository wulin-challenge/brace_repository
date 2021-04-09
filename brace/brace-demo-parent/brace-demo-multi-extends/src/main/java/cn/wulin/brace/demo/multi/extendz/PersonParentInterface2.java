package cn.wulin.brace.demo.multi.extendz;

import java.util.WeakHashMap;

/**
 * 人员父亲抽象类
 * @author wulin
 *
 */
public interface PersonParentInterface2 {
	WeakHashMap<PersonParentInterface2, PersonParentClass> reference = new WeakHashMap<>();
	
	/**
	 * 之类必须在构造方法中调用该方法
	 */
	default void constructionMethod(){
		PersonParentClass personParentClass = reference.get(this);
		if(personParentClass == null) {
			reference.put(this, new PersonParentClass());
		}
	}

	default String getNickname() {
		return reference.get(this).getNickname();
	}

	default void setNickname(String nickname) {
		reference.get(this).setNickname(nickname);
	}

	default Integer getSex() {
		return reference.get(this).getSex();
	}

	default void setSex(Integer sex) {
		reference.get(this).setSex(sex);
	}

	default Integer getHeight() {
		return reference.get(this).getHeight();
	}

	default void setHeight(Integer height) {
		reference.get(this).setHeight(height);
	}
}
