/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package ioc;

/**
 * ClassName: BeanDefinition
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public class BeanDefinition {

    private String beanName;
    private String clsPath;

    private  BeanDefinition(Builder builder) {
        this.beanName = builder.beanName;
        this.clsPath = builder.clsPath;
    }

    public static class   Builder  {
         String beanName;
        private String clsPath;

        public Builder() {
        }

        public Builder  beanName (String var){
            beanName = var;
            return this;
        }
        public Builder  cls (String  var){
            clsPath = var;
            return this;
        }
        public   BeanDefinition getInstance(){
            return new BeanDefinition(this);
        }
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClsPath() {
        return clsPath;
    }

    public void setClsPath(String clsPath) {
        this.clsPath = clsPath;
    }
}
