/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月22日
 */
package cn.withmes.springboot.singlepointjwt;

import org.joda.time.DateTime;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: MyController
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月22日
 */
@Controller
public class MyController {

    /**
     * @Description:模拟保存用户数据
     * @param:
     * @return:
     * @auther: liming
     * @date: 7/23/2019 7:26 PM
     */
    public static Map<String, User> userinfoMap = new ConcurrentHashMap<>(16);

    @PostConstruct
    public void init() {
        userinfoMap.put("zhangfei", new User("zhangfei", "123"));
    }


    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
    }


    @GetMapping("/error")
    public ModelAndView error(HttpServletRequest request) {
        String msg = request.getParameter("msg");
        ModelAndView view = new ModelAndView();
        view.addObject("msg", msg);
        return view;
    }


    /**
     * @Description:展示信息
     * @param:
     * @return:
     * @auther: liming
     * @date: 7/23/2019 7:25 PM
     */
    @RequestMapping(value = "/login/showinfo", method = RequestMethod.GET)
    @ResponseBody
    @JWTAnnotation
    public ModelAndView showinfo(String username) {
        if (StringUtils.isEmpty(username)) {
            ModelAndView info = new ModelAndView("error");
            info.addObject("errorMsg", "账号为空!");
            return info;
        }
        ModelAndView info = new ModelAndView("info");
        User user = userinfoMap.get(username);
        user.setPwd(null);
        info.addObject("Info", user);
        return info;
    }


    /**
     * @Description:登陆
     * @param:
     * @return:
     * @auther: liming
     * @date: 7/23/2019 7:25 PM
     */
    @GetMapping("/login/in")
    @ResponseBody
    public ResponseData<User> loginin(@NonNull String username, @NonNull String pwd, HttpServletResponse response) {
        ResponseData<User> data = null;
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(pwd)) {
            data = new ResponseData<>();
            data.setMsg("用户名或者密码不能为空");
            return data;
        }
        if (!userinfoMap.containsKey(username) || !userinfoMap.get(username).getPwd().equalsIgnoreCase(pwd))
            return new ResponseData<>(-1);
        data = createUserToken(username);
        response.addHeader("Set-Cookie", "aceess_token=" + data.getToken());
        return data;
    }


    /**
     * @Description:生成token
     * @param:
     * @return:
     * @auther: liming
     * @date: 7/23/2019 7:26 PM
     */
    public static ResponseData<User> createUserToken(@NonNull String username) {
        ResponseData<User> responseData = new ResponseData<>(-1);
        User user = userinfoMap.get(username);
        Map<String, Object> payload = new HashMap<>();
        payload.put("uukey", user.getUsername());
        payload.put("exp", DateTime.now().plusSeconds(40).toDate().getTime() / 1000);
        String token = JWTUtil.generatorToken(payload);
        responseData.setCode(0);
        responseData.setToken(token);
        responseData.setData(user);
        return responseData;
    }


}
