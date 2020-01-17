package cn.withme.pattern.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liming
 * @Description:
 * @date 2019年12月10日
 */
public class PigTest {

    private static Map<String,AbstractPig> map =   new HashMap<>();

    static {
        map.put("1", new RedPig());
    }

    public static void main(String[] args) {
        map.forEach((k,v)-> System.out.println(k+v));
    }
}
