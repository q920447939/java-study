/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月17日
 */
package cn.withmes.spring.boot.source.find;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.lang.annotation.*;

/**
 * ClassName: MyAnnotation
 * @Description:
 * @author leegoo
 * @date 2019年06月17日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Inherited
@Service //  标记@Service
// 可以修改里面的属性名字,然后使用自己定义的注解达到@Service的效果,
// 当然也可以多弄一个spring的元注解(或者是自己的注解,
// 只是spring的注解都有对应的实现方式,
// 而自己的注解需要自己实现拿到注解做对应的事情),

public @interface MyAnnotation {


    //给@Service 里面的属性 value 改一个自己喜欢的名字..
    //在我看来这样其实有一点伪集成的feel
    @AliasFor(annotation = Service.class,attribute = "value")
    String name();


}
