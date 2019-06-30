/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月30日
 */
package cn.withmes.spring.springboot.action.primarybean;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * ClassName: Chocolate
 * @Description:
 * @author leegoo
 * @date 2019年06月30日
 */
@Component
//@Primary  当Dessert的实现bean有多个的时候,使用@Primary 可以强制让spring 调用该实现类
public class Chocolate implements Dessert {

    @Override
    public void flavour() {
        System.err.println("巧克力的味道");
    }

}
