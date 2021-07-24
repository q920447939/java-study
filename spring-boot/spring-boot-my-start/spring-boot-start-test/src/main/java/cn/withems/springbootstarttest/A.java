/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年03月19日
 */
package cn.withems.springbootstarttest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClassName: A
 * @Description:
 * @author leegoo
 * @date 2021年03月19日
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class A {

    @Resource
    private B b;


    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "A{" +
                "b=" + b +
                '}';
    }
}
