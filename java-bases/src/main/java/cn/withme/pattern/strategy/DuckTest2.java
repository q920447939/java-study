package cn.withme.pattern.strategy;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author liming
 * @Description:
 * @date 2019年12月13日
 */
public class DuckTest2 {

    static List<Duck> list = new ArrayList<>();

    static {
        list.add(new Duck(1, "M-KW"));
        list.add(new Duck(2, "B-QCS"));
        list.add(new Duck(3, "M-WVM"));
    }

    static List<Duck> list2 = new ArrayList<>();

    static String sortList[] = new String[]{"M", "K", "L", "D", "C", "A", "B", "E", "F", "G", "H", "I", "J", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    public static void main(String[] args) throws ParseException {
       // System.out.println(list);
       sort1(list);
       // System.out.println(list);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s1 = "2017-08-10 22:18:22";
        String s2 = "2018-08-10 22:18:22";
        String s3 = "2017-08-10 22:19:22";
        List<Date> arr = new ArrayList<>();
        arr.add(df.parse(s1));
        arr.add(df.parse(s2));
        arr.add(df.parse(s3));
        arr.sort((o1,o2)->{
            return (int)(o2.getTime() - o1.getTime());
        });
        System.out.println(arr);
        // new->old排序，首先将字符型的日期转换为日期类型。
       /* arr.sort( (a1, a2) -> {
            try {
                return df.parse(a1).compareTo(df.parse(a2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 1;
        });*/
        //System.out.println(arr);
    }

    private static List<Duck> sort1(List<Duck> list) {
       list.sort(Comparator.comparing(Duck::getName).reversed());
        return list;
    }


    static List<Duck> sort(List<Duck> list) {
        list = list.parallelStream().sorted((a, b) -> {
            String aV = StringUtils.isBlank(a.getName()) ? "" : a.getName();
            String bV = StringUtils.isBlank(b.getName()) ? "" : b.getName();
            String aVlue = String.valueOf(aV.charAt(0)).toUpperCase();
            String bValue = String.valueOf(bV.charAt(0)).toUpperCase();
            int aIndex = doFindIndex(sortList, aVlue);
            int bIndex = doFindIndex(sortList, bValue);
            if (aIndex == bIndex) {
                //return -(new Date(a[secondAttr]).getTime() - new Date(b[secondAttr]).getTime()) * rev;
            } else {
                return (aIndex - bIndex);
            }
            return 0;
        }).collect(Collectors.toList());
        return list;
    }

    static int doFindIndex(String[] sortList, String value) {
        for (int i = 0; i < sortList.length; i++) {
            if (sortList[i].equalsIgnoreCase(value)) return i;
        }
        return -1;
    }


    public static void main2(String[] args) {
        Map<String, String> map = new TreeMap<String, String>();
        map.put("d", "ddddd");
        map.put("c", "ccccc");

        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        String string = String.valueOf(list.get(0)) + String.valueOf(list.get(1));
        System.out.println(string);

    }

}
