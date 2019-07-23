/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月22日
 */
package cn.withmes.springboot.singlepointjwt;

import com.sun.javafx.geom.transform.SingularMatrixException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: JWTInteruptHandler
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月22日
 */
@Configuration
public class JWTInteruptHandler extends HandlerInterceptorAdapter implements WebMvcConfigurer {

    private final String ACCESS_TOKEN = "aceess_token";

    //拦截路径  需要实现 WebMvcConfigurer
    // 可添加多个
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInteruptHandler()).addPathPatterns("/login/showinfo");
        registry.addInterceptor(new JWTInteruptHandler()).excludePathPatterns("/resource");
        registry.addInterceptor(new JWTInteruptHandler()).excludePathPatterns("/login/in");
        registry.addInterceptor(new JWTInteruptHandler()).excludePathPatterns("/index");
        registry.addInterceptor(new JWTInteruptHandler()).excludePathPatterns("/error");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = null;
        if (handler instanceof org.springframework.web.method.HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        } else {
            return true;
        }
        if (checkAnnotation(handlerMethod)) return true;

        String cookieValue = CookieUtils.getCookieValue(request, this.ACCESS_TOKEN);

        // 如果cookie 没有 token 那么跳转到首页
        if (StringUtils.isEmpty(cookieValue)) {
            response.sendRedirect("/index");
            return false;
        }

        //解析jwt
        try {
            Claims claims = JWTUtil.parseToken(cookieValue);
            System.out.println(claims);
        } catch (ExpiredJwtException e) {
            dispose(response, "expire token .");
            return false;
        } catch (SingularMatrixException e) {
            dispose(response, "sign exception .");
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    private void dispose(HttpServletResponse response, String msg) throws IOException {
        response.sendRedirect("/error?msg=" + msg);
    }

    /**
     * @Description:检查是否带有注解
     * @param:
     * @return:
     * @auther: liming
     * @date: 7/23/2019 7:27 PM
     */
    private boolean checkAnnotation(HandlerMethod handlerMethod) {
        return null == handlerMethod.getMethod().getAnnotation(JWTAnnotation.class);
    }
}
