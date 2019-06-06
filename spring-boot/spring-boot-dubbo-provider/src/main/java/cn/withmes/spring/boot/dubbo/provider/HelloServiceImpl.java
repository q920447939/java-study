/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月15日
 */
package cn.withmes.spring.boot.dubbo.provider;

/**
 * ClassName: HelloServiceImpl
 * @Description:
 * @author leegoo
 * @date 2019年05月15日
 */
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
