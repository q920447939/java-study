package com.example.springboot.myfactorybean.annotation;

import com.example.springboot.myfactorybean.register.FruitRegister;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: FruitAnnotation
 *
 * @author leegoo
 * @Description:
 * @date 2022年03月10日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(FruitRegister.class)
public @interface FruitAnnotation  {

}
