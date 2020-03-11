/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package ioc;


import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DefaultListableBeanFactory
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public class DefaultListableBeanFactory extends  AbstractListableBeanFactory{

    private static List<Reader> readers = new ArrayList<>();
    static {
        readers.add(new XmlBeanDefinitionReader());
    }
    public DefaultListableBeanFactory (String location) {
        if (null == location || "".equalsIgnoreCase(location)) return;
        this.register(location);
        postProcess();
    }

    private void register(String location) {
        for (Reader reader : readers) {
            //判断是哪种方式的解析规则,这里只有一种 classpath:
            if (reader.isRead(location)) {
                this.read(reader);
                return ;
            }
        }
    }

    private void read(Reader reader) {
        //加载document
        reader.loadBeanDefinition(this);
        return ;
    }

    @Override
    public Object getBean(String beanName) {
        return super.beanMap.get(beanName);
    }

    @Override
    public Object getBean(Class<?> clz) {
        return null;
    }


    @Override
    public void postProcess() {
        this.beanDefinition.forEach((k,v)->{
            this.registerBeanMap(k,v);
        });
    }

    private  void  registerBeanMap (String id,BeanDefinition clsPath) {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(clsPath.getClsPath());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (null != aClass){
            try {
                Object o = aClass.newInstance();
                this.beanMap.put(id,o);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
