
package com.smpl.base.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author liangxia@live.com
 */
public class SpringBeanManager implements BeanFactoryAware {

	private static BeanFactory beanFactory = null;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (SpringBeanManager.beanFactory == null) {
			SpringBeanManager.beanFactory = beanFactory;
		}
	}

	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public static <T> T getBean(Class<T> clazz) {
		if (beanFactory != null) {
			return beanFactory.getBean(clazz);
		}
		return null;
	}

	public static Object getBean(String beanName) {
		if (beanFactory != null) {
			return beanFactory.getBean(beanName);
		}
		return null;
	}
}
