/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年04月06日
 */
package xsd;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * ClassName: CustomNameSpaceHandler
 * @Description:
 * @author leegoo
 * @date 2020年04月06日
 */
public class CustomNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("user", new UserDefinitionParser());
    }
}
