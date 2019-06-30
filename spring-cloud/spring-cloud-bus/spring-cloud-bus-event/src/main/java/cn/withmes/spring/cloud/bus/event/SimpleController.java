/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月28日
 */
package cn.withmes.spring.cloud.bus.event;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ClassName: SimpleController
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月28日
 */
@RestController
public class SimpleController implements ApplicationEventPublisherAware, ApplicationContextAware {
    private ApplicationEventPublisher publisher;

    private ApplicationContext applicationContext;

    //private DiscoveryClient client;

    @GetMapping("/event")
    public void event(String beanname, String property,Object value) {
        //client.getInstances("");
        publisher.publishEvent(new UpdateBeanProperty(beanname, property,value));
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @EventListener
    public void publishEvent(PayloadApplicationEvent<UpdateBeanProperty> event) {
        UpdateBeanProperty updateBeanProperty = event.getPayload();
        Object bean = applicationContext.getBean(updateBeanProperty.getBeanname());
        System.out.println(12);
        try {
            Class<?> aClass = Class.forName(bean.getClass().getCanonicalName());
            Field field = aClass.getDeclaredField(updateBeanProperty.getProperty());
            field.setAccessible(true);
            String fistUpdate = updateBeanProperty.getProperty().substring(0, 1).toUpperCase();
            String property = fistUpdate + updateBeanProperty.getProperty().substring(1, updateBeanProperty.getProperty().length() );
            Method method = aClass.getMethod("set" +property , field.getType());
            method.invoke(bean,updateBeanProperty.getValue());
            System.out.println(bean);
            //field.set(, );
        } catch (ClassNotFoundException | NoSuchFieldException |NoSuchMethodException | IllegalAccessException |InvocationTargetException  e) {
            e.printStackTrace();
        }


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    class UpdateBeanProperty {
        String beanname;
        String property;
        Object value;

        public UpdateBeanProperty(String beanname, String property, Object value) {
            this.beanname = beanname;
            this.property = property;
            this.value = value;
        }


        public String getBeanname() {
            return beanname;
        }

        public void setBeanname(String beanname) {
            this.beanname = beanname;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
