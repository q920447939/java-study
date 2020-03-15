/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月14日
 */
package cn.withmes.springboot.my.aop.aop;

import cn.withmes.springboot.my.aop.scan.MyCTransaction;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * ClassName: MyAOPScan
 *
 * @author leegoo
 * @Description:
 * @date 2020年03月14日
 */
public class MyAOPScan extends ClassPathBeanDefinitionScanner {

    public MyAOPScan(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        //添加过滤条件，这里是只添加了@MyCTransaction的注解才会被扫描到 ,  注意此处的注解的标记在类上面的, 拥有@MyCTransaction注解,我们自己把这个class注入到spring 容器
        addIncludeFilter(new AnnotationTypeFilter(MyCTransaction.class));
        return super.doScan(basePackages);
    }

}
