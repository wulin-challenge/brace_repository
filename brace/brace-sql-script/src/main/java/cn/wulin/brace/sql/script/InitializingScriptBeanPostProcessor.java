package cn.wulin.brace.sql.script;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.PriorityOrdered;

import cn.wulin.brace.sql.script.dao.SqlScriptDao;

/**
 * 专门用来处理创建表,启动初始化数据的处理器
 * @author wulin
 *
 */
public class InitializingScriptBeanPostProcessor implements BeanPostProcessor,PriorityOrdered,BeanFactoryAware{
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializingScriptBeanPostProcessor.class);
			
	private DefaultListableBeanFactory beanFactory;
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
			
			String[] beanNames = beanFactory.getBeanNamesForType(InitializingScript.class);
			
			if(beanNames.length == 0) {
				LOGGER.debug("没有 InitializingScript 的bean");
			}else {
				try {
					InitializingScript initializingScript = beanFactory.getBean(InitializingScript.class);
					initializingScript.initializing(resourceScript,sqlScriptDao);
				} catch (Exception e) {
					LOGGER.error("InitializingScript 执行出错了!",e);
				}
			}
		}
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}
	
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
	

}
