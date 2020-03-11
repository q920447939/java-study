/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package ioc;

/**
 * ClassName: Beanfactory
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public interface Beanfactory {

    Object getBean(String beanName);

    Object getBean(Class<?> clz);
}
