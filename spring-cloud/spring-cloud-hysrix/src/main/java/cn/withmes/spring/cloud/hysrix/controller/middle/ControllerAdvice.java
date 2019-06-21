/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月20日
 */
package cn.withmes.spring.cloud.hysrix.controller.middle;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: ControllerAdvice
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月20日
 */
@RestControllerAdvice(basePackageClasses = MiddleController.class)
public class ControllerAdvice {


    @ExceptionHandler(value = {TimeoutException.class})
    public void customException(Writer writer, TimeoutException ex) throws IOException {
        writer.write("超时了..");
        writer.flush();
        writer.close();
    }


}
