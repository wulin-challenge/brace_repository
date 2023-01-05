package cn.wulin.brace.sql.script;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;

import cn.wulin.brace.sql.script.dao.SqlScriptDao;

/**
 * 专门用来处理创建表,启动初始化数据的处理器
 * @author wulin
 *
 */
public class InitializingScriptBeanPostProcessor implements BeanPostProcessor,PriorityOrdered,BeanFactoryAware{
	private BeanFactory beanFactory;
	private ResourceScript resourceScript;
	private AtomicBoolean inited = new AtomicBoolean(false);

	public InitializingScriptBeanPostProcessor() {
		super();
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(inited.compareAndSet(false, true)) {
			resourceScript = beanFactory.getBean(ResourceScript.class);
			
			SqlScriptDao sqlScriptDao = resourceScript.getSqlScriptDao();
			InitializingScript initializingScript = beanFactory.getBean(InitializingScript.class);
			initializingScript.initializing(resourceScript,sqlScriptDao);
		}
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}
	
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	

}
