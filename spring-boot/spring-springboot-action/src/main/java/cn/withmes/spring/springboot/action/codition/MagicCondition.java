/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月30日
 */
package cn.withmes.spring.springboot.action.codition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Random;

/**
 * ClassName: MagicCondition
 * @Description:
 * @author leegoo
 * @date 2019年06月30日
 */

public class MagicCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //模拟条件注册   返回false 会导致spring 启动失败
//        int i = new Random().nextInt(5);
//        System.err.println("i:"+i);
//        if (i >0) {
//            return true;
//        }
        return true;
    }
}
