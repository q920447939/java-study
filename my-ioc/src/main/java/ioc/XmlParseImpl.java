/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package ioc;

/**
 * ClassName: XmlParseImpl
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public class XmlParseImpl implements  Parser {
    private  Beanfactory beanfactory;

    public XmlParseImpl(Beanfactory beanfactory) {
        this.beanfactory = beanfactory;
    }

    @Override
    public void parse() {

    }
}
