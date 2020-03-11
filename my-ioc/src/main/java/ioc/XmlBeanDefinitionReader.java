/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package ioc;


import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.List;

import static utils.DocumentUtils.createDocument;

/**
 * ClassName: XmlBeanDefinitionReader
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public class XmlBeanDefinitionReader implements  Reader {
    private static final String PRE = "classpath:";
    private String location ;

    private AbstractListableBeanFactory abstractListableBeanFactory;

    @Override
    public boolean isRead(String location) {
        this.location = location.replace(PRE, "");
        return location.startsWith(PRE);
    }

    @Override
    public InputStream getResourceLoader() {
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }

    @Override
    public void loadBeanDefinition( AbstractListableBeanFactory beanfactory) {
        if ( null == beanfactory) return;
        this.abstractListableBeanFactory = beanfactory;

        Document document = createDocument(this.getResourceLoader());
        if (null == document) return;
        Element rootElement = document.getRootElement();
        //获取当前元素下的全部子元素
        List<Element> childElements = rootElement.elements();
        for (Element bean : childElements) {//循环输出全部book的相关信息
            this.disposeBeanSelf(bean);
        }
    }

    private void disposeBeanSelf(Element bean) {
        Attribute attribute = bean.attribute("id");
        String beanId = attribute.getStringValue();
        Attribute aClass = bean.attribute("class");
        BeanDefinition definition = new BeanDefinition.Builder().beanName(beanId).cls(aClass.getStringValue()).getInstance();
        this.abstractListableBeanFactory.registerBeanDefinition(beanId,definition);
    }
}
