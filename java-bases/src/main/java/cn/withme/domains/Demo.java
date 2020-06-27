/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月22日
 */
package cn.withme.domains;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ClassName: Demo
 * @Description:
 * @author leegoo
 * @date 2020年06月22日
 */
public class Demo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<String> strings = new ArrayList<>();
            for (int i1 = 0; i1 < 3; i1++) {
                strings.add(i+""+i1);
            }
            list.add(strings);
        }
        System.out.println(list);

        List<List<String>> list2 = new ArrayList<>();
         Stream.of(0, 1, 2).flatMap(k -> Stream.of(0, 1, 2).map(k1 ->
                {
                    List<String> strings1 = new ArrayList<>();
                    strings1.add(k + "" + k1);
                    return strings1;
                }
        )).forEach(k->{
             list2.add(k);
         });
        System.out.println(list2);
    }

    public static Fruit<Location>  aaa (String str){
        return JSONObject.parseObject(str, Fruit.class);
    }
}
