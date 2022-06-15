package com.example.springboot.myfactorybean.myfactory;

import org.springframework.beans.factory.FactoryBean;

/**
 * ClassName: FruitFactoryBean
 *
 * @author leegoo
 * @Description:
 * @date 2022年03月10日
 */
public class FruitFactoryBean<T> implements FactoryBean<T> {
    private String beanName;

    public FruitFactoryBean(String beanName,T t) {
        this.beanName = beanName;
        FactoryBeanMap.put(beanName,t);
    }

    @Override
    public T getObject() throws Exception {
        return (T) FactoryBeanMap.get(beanName);
    }

    @Override
    public Class<?> getObjectType() {
        return FactoryBeanMap.get(beanName).getClass();
    }


    @Override
    public boolean isSingleton() {
        return false;
    }
}
