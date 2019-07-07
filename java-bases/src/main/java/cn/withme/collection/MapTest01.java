/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月07日
 */
package cn.withme.collection;


import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: MapTest01
 * @Description:
 * @author leegoo
 * @date 2019年07月07日
 */
public class MapTest01 {
    public static void main(String[] args) {
        Map<String,String> map = new ConcurrentHashMap<>();
        map.put("key", "value1");
        map.put("key2", "value2");
    }
}
