package cn.withme.pattern.strategy;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liming
 * @Description:
 * @date 2019年11月22日
 */
@Setter
@Getter
public class Duck {

    private int leg;

    private String name;

    private int head;

    public Duck() {

    }

    public Duck(int leg, String name) {
        this.leg = leg;
        this.name = name;
    }

    public Duck(int leg, String name, int head) {
        this.leg = leg;
        this.name = name;
        this.head = head;
    }

    public static void main(String[] args) {
       /* List<Integer> result = new ArrayList<>();
        List<Integer> list = Arrays.asList(1,2,3,4,5,5,6,7,8,9,0);
        List<List<Integer>> parts = Lists.partition(list, 5);
        parts.forEach(k-> result.addAll(k));
        System.out.println(JSONObject.toJSONString(parts));*/
      /* List<String> list = new ArrayList<>();
       test(list);
        System.out.println(list);*/
        List<Duck> list = new ArrayList<>();
       /* list.add(new Duck(1, "A:1", 1));
        list.add(new Duck(1, "A:2", 1));
        list.add(new Duck(1, "B:1", 2));
        list.add(new Duck(11, "B:2", 1));
        list.add(new Duck(12, "C:3", 2));*/

      /*  String collect = list.stream().map(s -> s.getName()).map(s -> {
            String[] strs = s.split(":");
            return strs[0] + ":" + strs[1];
        }).collect(Collectors.joining(","));
        System.out.println(collect);*/
        /*TreeSet<Duck> ducks = new TreeSet<>((k1, k2) -> k1.getName().compareTo(k2.getName()));

        List<Duck> unique = list.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Duck::getName))),
                        ArrayList::new));
        System.out.println(unique);*/

        /*TreeSet<Duck> ducks = new TreeSet<>(Comparator.comparing(Duck::getLeg));
        ducks.addAll(list);
        System.out.println(ducks);*/
        //ducks.addAll(list);
        //System.out.println(ducks);
        // list.stream().
        /*Map<Integer, Map<String, List<Duck>>> collect = list.stream().collect(Collectors.groupingBy(k -> k.getLeg(),
                Collectors.groupingBy(duck -> {
                            return duck.getName();
                        }
                )));
        System.out.println(list);

        list.forEach(k->{
            if (k.getLeg() == 1) return;
            System.out.println(k);
        });*/
        list.add(new Duck(13, null, 2));
        System.out.println(list+"=============");

        List<Duck> collect = list.stream().collect(Collectors.toList());
        Duck duck = collect.get(0);
        duck.setName("name");
        System.out.println(list);
    }

    public static String test(String  str) {
        if (!str.contains("_")){
            return  str ;
        }
        List<String> collect = Stream.of(str.split("_")).collect(Collectors.toList());
        List<String> result  = new ArrayList<>(collect.size()) ;
        result.add(collect.get(0));
        for (int i = 1; i < collect.size(); i++) {
            result.add(captureName(collect.get(i)));
        }
        return String.join("", result);
    }

    /**
     * 将字符串的首字母转大写
     * @param str 需要转换的字符串
     * @return
     */
    private static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    @Override
    public String toString() {
        return "Duck{" +
                "leg=" + leg +
                ", name='" + name + '\'' +
                '}';
    }
}
