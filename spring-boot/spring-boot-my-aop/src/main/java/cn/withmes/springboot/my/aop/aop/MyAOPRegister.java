/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月14日
 */
package cn.withmes.springboot.my.aop.aop;

import cn.withmes.springboot.my.aop.proxy.MyJDKproxy;
import cn.withmes.springboot.my.aop.scan.MyTransaction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: MyAOP
 * @Description:
 * @author leegoo
 * @date 2020年03月14日
 */
public class MyAOPRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanFactoryAware {
    private ResourceLoader resourceLoader;

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,@NonNull BeanDefinitionRegistry registry) {
        //这里是获取cn.withmes.springboot.my.aop.SpringBootMyAopApplication类上对应的注解
        //MergedAnnotations annotations = importingClassMetadata.getAnnotations();
        //这里判断是否存在MyAOP注解
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(MyAOP.class.getName()));
        if (mapperScanAttrs == null)  return;

        Set<BeanDefinitionHolder> beanDefinitionHolders = this.registerBeanDefinitions(mapperScanAttrs, registry);
        this.registerProxy(registry,beanDefinitionHolders);
    }

    private void registerProxy(BeanDefinitionRegistry registry,Set<BeanDefinitionHolder> beanDefinitionHolders) {
        if (null == beanDefinitionHolders) return;
        beanDefinitionHolders.forEach(k->{
            try {
                //这里获取到cn.withmes.springboot.my.aop.service.UserServiceImpl类
                String clzPath = k.getBeanDefinition().getBeanClassName();
                Class<?> aClass = Class.forName(clzPath);
                Method[] methods = aClass.getMethods();
                if (methods.length > 0) {
                    for (Method method : methods) {
                        MyTransaction ma = method.getAnnotation(MyTransaction.class);
                        if (null == ma || !ma.proxyFlag() || StringUtils.isEmpty(ma.callBackMethod())) continue;
                        //这里会有Object 的方法,这里直接过滤掉Obj的方法
                        if (!method.getName().equalsIgnoreCase("findUser")) continue;
                        //动态代理
                        //参数解释 1.需要代理类的类加载器 2,需要代理类的实现的接口  还有一个自定义的handler
                        Object proxy = Proxy.newProxyInstance(aClass.getClassLoader(),aClass.getInterfaces(),  new MyJDKproxy(aClass,ma,this.beanFactory) );
                        if (registry instanceof SingletonBeanRegistry) {
                            SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry)registry;
                            //这里将我们代理的对象放入到spring 容器中
                            //此处放入 不会执行init方法 因为已经过了初始化
                            singletonBeanRegistry.registerSingleton( k.getBeanName()+new Random().nextInt(),proxy);
                        }
                    }
                }
                //获取到注解name的值并返回
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private  Set<BeanDefinitionHolder> registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        List<String> basePackages = new ArrayList<>();
        //取到所有属性的值
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
        MyAOPScan scanner = new MyAOPScan(registry);
        //
        //scanner.setBeanNameGenerator(new MyBeanNameGenerator());
        if(resourceLoader != null){
            scanner.setResourceLoader(resourceLoader);
        }
        return scanner.doScan(StringUtils.toStringArray(basePackages));
    }



}
