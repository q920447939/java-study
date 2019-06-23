package cn.withmes.springcloud.open.fegin.client.advance;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MyFeignCRegistrar.class)
public @interface EnableMyFeignC {

    //找到所有带有@MyFeignClient的接口
    Class<?>[] clients() default {};
}
