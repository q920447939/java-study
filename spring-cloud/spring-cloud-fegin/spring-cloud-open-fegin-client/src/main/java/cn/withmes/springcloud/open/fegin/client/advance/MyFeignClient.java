package cn.withmes.springcloud.open.fegin.client.advance;



import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MyFeignClient {

    String name() default "";
}
