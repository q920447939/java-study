/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月23日
 */
package cn.withmes.springcloud.open.fegin.client.advance;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * ClassName: RequestRestTemplateInvocationHandler
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月23日
 */
public class RequestRestTemplateInvocationHandler implements InvocationHandler {


    private final String serverName;


    private final BeanFactory beanFactory;



    //构造器将 serverName 传入
    public RequestRestTemplateInvocationHandler(String serverName, BeanFactory beanFactory) {
        this.serverName = serverName;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取到所有带@RequestMapping注解的方法,因为只有这些方法才需要发送请求
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);


        if (null != getMapping) {
            //获取到uri
            String[] uri = getMapping.value();

            StringBuilder fullUrl = new StringBuilder();

            //http://{url}/{serverName}
            fullUrl.append("http://").append(serverName).append(uri[0]);

            //获取方法参数的个数
            int count = method.getParameterCount();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < count; i++) {
                //获取到类上面的参数类型和名称
                Class<?> parameterType = method.getParameterTypes()[i];

                //获取method 所有的注解
                //为什么是二维数组
                //因为方法上面可以打多个注解.一个注解里面可以放多个属性
                String paramName = "";
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                if (parameterAnnotations.length > 0) {
                    for (Annotation a2 : parameterAnnotations[i]) {
                        if (a2 instanceof RequestParam) {
                            RequestParam requestParam = (RequestParam) a2;
                            paramName = requestParam.value();
                            break;
                        }
                    }
                }

                String paramsValue = (String)args[i];

                //如果注解有值,那么就取注解的值, @RequestParam("message") String message
                // 否则就用默认的值 String message

                sb.append("&")
                  .append(paramName)
                  .append("=")
                  .append(paramsValue);

            }
            String queryStr = sb.toString();
            if (StringUtils.isNotBlank(queryStr)) {
                fullUrl.append("?").append(queryStr);
            }

            //获取RestTemplate bean
            RestTemplate restTemplateBean = beanFactory.getBean("restTemplateBean", RestTemplate.class);
            return restTemplateBean.getForObject(fullUrl.toString(),method.getReturnType());

        }


        return null;
    }
}
