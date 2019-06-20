/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月20日
 */
package cn.withmes.spring.cloud.hysrix.demo.controller.middle;

import org.springframework.stereotype.Component;

/**
 * ClassName: MyException
 * @Description:
 * @author leegoo
 * @date 2019年06月20日
 */
public class MyException extends  Exception {

    public MyException(String message) {
        super(message);
    }
}
