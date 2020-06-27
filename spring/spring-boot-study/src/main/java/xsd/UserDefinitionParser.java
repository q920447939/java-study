/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年04月06日
 */
package xsd;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * ClassName: UserDefinitionParser
 * @Description:
 * @author leegoo
 * @date 2020年04月06日
 */
public class UserDefinitionParser  extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return User.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
       String userName = element.getAttribute("userName");
       String email = element.getAttribute("email");
        bean.addPropertyValue("userName",userName);
        bean.addPropertyValue("email",email);
    }
}
