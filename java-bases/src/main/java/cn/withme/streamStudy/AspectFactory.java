/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年07月23日
 */
package cn.withme.streamStudy;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ClassName: AspectFactory
 * @Description:
 * @author leegoo
 * @date 2021年07月23日
 */
public interface AspectFactory {

    /**
     * @Description:这个方法是一一个切面方法,会返回一个proxy对象.当调用对象的方法时,会先调用proxy的before方法.最后会调用after方法
     * @param: [t]
     * @return: T
     * @auther: liming
     * @date: 2021/7/24 10:29
     */
    static <T> T newInstance(T t) throws Exception {

        Class aClass = Stream.of(t.getClass().getAnnotations()).filter(inter -> {
            return inter instanceof Aspect;
        }).map(aspe -> {
            return ((Aspect) aspe).clz();
        }).findFirst().get();
        Record newInstance = (Record)aClass.newInstance();
        Object newProxyInstance = Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                newInstance.before();
                Object invoke = method.invoke(t,args);
                newInstance.after();
                return invoke;
            }
        });
        return (T)newProxyInstance;
    }
}
