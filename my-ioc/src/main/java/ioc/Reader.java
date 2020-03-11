/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package ioc;


import java.io.InputStream;

/**
 * ClassName: Reader
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public interface Reader {
    boolean isRead(String location);
    InputStream getResourceLoader();

    void loadBeanDefinition(AbstractListableBeanFactory beanfactory);
}
