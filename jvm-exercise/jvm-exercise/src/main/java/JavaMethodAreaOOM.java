/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年01月30日
 */

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 * VM Args：-XX:PermSize=10M -XX:MaxPermSize=10M
 * JDK7 运行一段时候后出现
 *  Caused by: java.lang.OutOfMemoryError: PermGen space（未测试）
 *JDK8 运行一段时候后出现
 *  Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
 *
 *  VM Args：-XX:MaxMetaspaceSize=10M
 *  JDK8 运行一段时候后出现
 *  Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
 * @author zzm
 */
public class JavaMethodAreaOOM {
    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            enhancer.create();
        }
    }
    static class OOMObject {
    }
}
