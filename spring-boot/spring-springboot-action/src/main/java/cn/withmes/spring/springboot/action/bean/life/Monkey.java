/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月28日
 */
package cn.withmes.spring.springboot.action.bean.life;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

/**
 * ClassName: Monkey
 * @Description: 测试bean的生命过程
 * @author leegoo
 * @date 2019年06月28日
 */
@Component
public class Monkey
        implements BeanNameAware   //

{


    @Override
    public void setBeanName(String id) {
        System.err.println("bean 生命流程第一步 :"+id);
    }
}
