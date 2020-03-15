/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月14日
 */
package cn.withmes.springboot.my.aop.customer.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: MyAOP
 * @Description:
 * @author leegoo
 * @date 2020年03月14日
 */
public class CustomerScanRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanFactoryAware {
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
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(CustomerScan.class.getName()));
        if (mapperScanAttrs == null)  return;

        this.registerBeanDefinitions(mapperScanAttrs, registry);
    }


    private  Set<BeanDefinitionHolder> registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        List<String> basePackages = new ArrayList<>();
        //取到所有属性的值
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
        CustomerScanner scanner = new CustomerScanner(registry);
        scanner.setBeanNameGenerator(( beanDefinition,beanDefinitionRegistry)->{
            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Class<?> clz = Class.forName(beanClassName);
                MyService at = clz.getAnnotation(MyService.class);
                if (null == at) return null;
                if (at.name().equalsIgnoreCase("")  ) {
                    String clzSimpleName = clz.getSimpleName();
                    String first = String.valueOf(clzSimpleName.charAt(0));
                    return clzSimpleName.replaceFirst(first,first.toLowerCase());
                }
                return at.name();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        });
        if(resourceLoader != null){
            scanner.setResourceLoader(resourceLoader);
        }
        return scanner.doScan(StringUtils.toStringArray(basePackages));
    }



}
