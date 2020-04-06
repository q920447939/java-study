/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年04月05日
 */
package demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * ClassName: HelloServiceInjectProcessor
 * @Description:
 * @author leegoo
 * @date 2020年04月05日
 */
@Component
public class HelloServiceInjectProcessor implements BeanPostProcessor{


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetCls = bean.getClass();
        Field[] targetFld = targetCls.getDeclaredFields();
        if (beanName.contains("messageImpl")){
            for (Field field : targetFld) {
                //找到制定目标的注解类
                if (field.isAnnotationPresent(MyService.class)) {
                    if (!field.getType().isInterface()) {
                        throw new BeanCreationException("RoutingInjected field must be declared as an interface:" + field.getName()
                                + " @Class " + targetCls.getName());
                    }
                    try {
                        this.handleRoutingInjected(field, bean, field.getType());
                    } catch (IllegalAccessException | IOException | NoSuchMethodException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return bean;
    }

    private void handleRoutingInjected(Field field, Object bean, Class<?> targetCls) throws IllegalAccessException, IOException, NoSuchMethodException, ClassNotFoundException {
        field.setAccessible(true);
        User user = (User) Proxy.newProxyInstance(bean.getClass().getClassLoader(), new Class[]{targetCls}, new MyInvocationHandler(field));
        field.set(bean,user);
    }

}