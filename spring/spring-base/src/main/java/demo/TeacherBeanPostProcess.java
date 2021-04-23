/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年04月23日
 */
package demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName: TeacherBeanFactory
 *
 * @author leegoo
 * @Description:通过BeanFactoryPostProcessor 动态创建bean
 * @date 2021年04月23日
 */
@Component
public class TeacherBeanPostProcess implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        /*DefaultListableBeanFactory df = (DefaultListableBeanFactory)configurableListableBeanFactory;
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Teacher.class);
        builder.addPropertyValue("name","王老师");
        String beanName = "teacher";
        df.registerBeanDefinition(beanName, builder.getRawBeanDefinition());*/
        //df.registerSingleton(beanName,new Teacher());
    }

    public void setLocation(String location) {

    }
}
