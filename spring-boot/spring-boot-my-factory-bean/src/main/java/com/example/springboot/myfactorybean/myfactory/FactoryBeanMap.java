package com.example.springboot.myfactorybean.myfactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: FactoryBeanMap
 *
 * @author leegoo
 * @Description:
 * @date 2022年03月10日
 */
public class FactoryBeanMap {
    private static final Map<String,Object> OBJECT_MAP = new HashMap<>();

    public static void put (String beanName,Object value) {
        OBJECT_MAP.put(beanName, value);
    }

    public static Object get(String beanName){
        return OBJECT_MAP.get(beanName);
    }
}
