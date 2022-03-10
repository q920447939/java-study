package com.example.springboot.myfactorybean.register;

import com.example.springboot.myfactorybean.annotation.FruitAnnotation;
import com.example.springboot.myfactorybean.myfactory.FruitFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Set;

/**
 * ClassName: FruitRegister
 *
 * @author leegoo
 * @Description:
 * @date 2022年03月10日
 */
public class FruitRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware , EnvironmentAware {

    private ResourceLoader resourceLoader;
    private BeanDefinitionRegistry registry;
    private Environment environment;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        this.registry = registry;

        //扫描含有自定义注解FooAnnotation的的所有类
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false, environment);
        provider.addIncludeFilter(new AnnotationTypeFilter(FruitAnnotation.class));
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents("com.example.springboot.myfactorybean.bean");

        for (BeanDefinition candidateComponent : candidateComponents) {
            AnnotationMetadata annotationMetadata = ((AnnotatedBeanDefinition) candidateComponent).getMetadata();
            String className = annotationMetadata.getClassName();
            try {
                Class<?> clz = ClassUtils.forName(className,this.getClass().getClassLoader());
                Object obj = clz.getDeclaredConstructor().newInstance();

                BeanDefinitionBuilder definition = BeanDefinitionBuilder
                        .genericBeanDefinition(FruitFactoryBean.class,()->{
                            return new FruitFactoryBean(className, obj);
                        });
                AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
                //在这里把FooFactoryBean与Bean绑定起来
                BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
                BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }




}
