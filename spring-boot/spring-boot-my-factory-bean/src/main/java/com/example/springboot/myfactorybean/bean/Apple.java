package com.example.springboot.myfactorybean.bean;

import com.example.springboot.myfactorybean.annotation.FruitAnnotation;
import org.springframework.stereotype.Service;

/**
 * ClassName: Apple
 *
 * @author leegoo
 * @Description:
 * @date 2022年03月10日
 */
@FruitAnnotation
public class Apple {

    public String echoMessage(String message) {
        return "ECHO:" + message;
    }

}
