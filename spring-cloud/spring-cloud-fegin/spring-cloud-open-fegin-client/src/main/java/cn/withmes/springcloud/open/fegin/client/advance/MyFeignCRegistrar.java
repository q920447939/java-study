/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月23日
 */
package cn.withmes.springcloud.open.fegin.client.advance;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * ClassName: MyFeignCRegistrar
 *
 * @author leegoo
 * @Description: 将所有@MyFeignClient 找到,并且通过动态代理的方式执行RestTemplate
 * @date 2019年06月23日
 */
public class MyFeignCRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware {


    // 实现BeanFactoryAware接口,得到 beanFactory
    private BeanFactory beanFactory;


    //获取环境变量
    private Environment environment;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
                                        BeanDefinitionRegistry registry) {

        ClassLoader classLoader = annotationMetadata.getClass().getClassLoader();


        //获取@EnableMyFeignC 注解
        Map<String, Object> attributes =
                annotationMetadata.getAnnotationAttributes(EnableMyFeignC.class.getName());

        if (null == attributes) return;

        //获取@EnableMyFeignC 里面的client属性
        Class<?>[] clientsClass = (Class<?>[]) attributes.get("clients");

        //获取带有@MyFeignClient的注解,并且需要是接口
        Stream.of(clientsClass)
                //找到所有带有@MyFeignClient的接口或者类
                .filter(interclass -> AnnotationUtils.findAnnotation(interclass, MyFeignClient.class) != null)
                .filter(Class::isInterface) //找到所有的接口
                .forEach(myFeignClientClass ->
                        {
                            //获取@MyFeignClient元信息
                            MyFeignClient myFeignClientAnnotation = findAnnotation(myFeignClientClass,
                                    MyFeignClient.class);
                            if (null == myFeignClientAnnotation) return;

                            //获取到服务器名字
                            //environment.resolvePlaceholders(myFeignClientAnnotation.name()) 会从配置文件里面拉取配置
                            String serverName = environment.resolvePlaceholders(myFeignClientAnnotation.name());

                            //JDK动态代理
                            //动态代理实现执行代理类的方法
                            Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{myFeignClientClass},
                                    new RequestRestTemplateInvocationHandler(serverName, beanFactory));


                            //将带有@MyFeignClient 注册为bean

                            String beanName = "myFeignClient" +serverName ;


                            //注入一个带有@MyFeignClient 的class 到spring 容器中
                            //方式一
                            //registerMyFeignClientBean(beanName, proxy, registry, myFeignClientClass);

                           //方式二
                            if (registry instanceof SingletonBeanRegistry) {
                                SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry)registry;
                                singletonBeanRegistry.registerSingleton(beanName,proxy);
                            }
                        }
                );

    }


    private void registerMyFeignClientBean(String beanName,
                                           Object proxy,
                                           BeanDefinitionRegistry registry,
                                           Class<?> myFeignClientClass
    ) {


        //动态注入一个bean ,
        /*
          在xml 文件中
          <bean id="beanName1">
            <constructor-arg> k1 </constructor-arg>
            <constructor-arg> k2 </constructor-arg>
            ...
          </bean>
         */
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(
                RestBeanRegister.class
        );

        // 构造器注入bean的属性
        beanDefinitionBuilder.addConstructorArgValue(proxy);
        beanDefinitionBuilder.addConstructorArgValue(myFeignClientClass);


        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();

        //完成bean的注册

        registry.registerBeanDefinition(beanName, beanDefinition);

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    public static class RestBeanRegister implements FactoryBean {
        private final Object proxy;
        private final Class<?> restClass;

        public RestBeanRegister(Object proxy, Class<?> restClass) {
            this.proxy = proxy;
            this.restClass = restClass;
        }


        @Override
        public Object getObject() throws Exception {
            return proxy;
        }

        @Override
        public Class<?> getObjectType() {
            return restClass;
        }
    }
}
