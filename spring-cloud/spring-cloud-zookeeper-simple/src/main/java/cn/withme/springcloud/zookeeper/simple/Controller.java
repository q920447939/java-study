/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月18日
 */
package cn.withme.springcloud.zookeeper.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * ClassName: Controller
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月18日
 */
@RestController
public class Controller {


    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("invoke/{servierName}/hello")
    public Object getServerHello(@PathVariable String servierName) {
        if (StringUtils.isEmpty(servierName)) {
            return "servierName 为空";
        }
        return restTemplate.getForObject("/" + servierName + "/hello", String.class);
    }




    @Autowired
    @CustomLoadBalance
    private RestTemplate loadTemplate;

    @GetMapping("load/invoke/{servierName}/hello")
    public Object loadBanlanceServerHello(@PathVariable String servierName) {
        if (StringUtils.isEmpty(servierName)) {
            return "servierName 为空";
        }
        return loadTemplate.getForObject("/" + servierName + "/hello", String.class);
    }




    @Autowired
    @Qualifier(value = "user1")
    User user;
    @GetMapping("say")
    public Object say() {
        return user;
    }



}
