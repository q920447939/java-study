/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年03月17日
 */
package cn.withems.springbootstarttest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * ClassName: InitBean
 * @Description:
 * @author leegoo
 * @date 2021年03月17日
 */
@Component
public class InitBean implements BeanDefinitionRegistryPostProcessor, BeanFactoryPostProcessor, BeanNameAware , Aware {

    private ConfigurableListableBeanFactory context;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.err.println("执行BeanFactoryPostProcessor");
        this.context = beanFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.err.println("执行BeanDefinitionRegistry");
    }

    @Override
    public void setBeanName(String name) {
        System.err.println("执行setBeanName"+name);
    }
}
