/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package ioc;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: AbstractListableBeanFactory
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public abstract class AbstractListableBeanFactory  implements Beanfactory{
     Map<String, BeanDefinition> beanDefinition = new HashMap<>();
     Map<String, Object> beanMap = new HashMap<>();

     void registerBeanDefinition(String id, BeanDefinition definition) {
        this.beanDefinition.put(id,definition);
    }


    public abstract void postProcess();
}
