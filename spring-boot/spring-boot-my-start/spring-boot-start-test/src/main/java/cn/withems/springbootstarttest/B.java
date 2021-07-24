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
 * ClassName: B
 * @Description:
 * @author leegoo
 * @date 2021年03月19日
 */
@Component
public class B {

    @Resource
    private A a;


}
