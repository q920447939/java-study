package cn.withme.springboot.web.demo;

import cn.withmes.spring.springboot.action.SpringSpringbootActionApplication;
import cn.withmes.spring.springboot.action.injuryproperty.Cafe;
import cn.withmes.spring.springboot.action.primarybean.Chocolate;
import cn.withmes.spring.springboot.action.primarybean.Dessert;
import cn.withmes.spring.springboot.action.service.Task;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.NotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= SpringSpringbootActionApplication.class)

public class SpringBootWebDemo01ApplicationTests implements BeanFactoryAware {

    @Autowired
    private   BeanFactory beanFactory;

    @Autowired
    private Task task;

    @Test
    public void contextLoads() {
        task.execute();
    }


    @Autowired
    @Qualifier(value = "chocolate") //限定词,如果Dessert存在多个实现类bean 等于在bean容器指定唯一的那个实现bean 此处指定的是Chocolate.class
    private Dessert dessert;

    @Test
    public void primarybean() {
        dessert.flavour();
    }





    @Test
    public void injuryproperty() {
        Cafe bean = beanFactory.getBean(Cafe.class);
        System.err.println("bean:"+bean);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
