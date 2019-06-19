
package cn.withme.string;

import java.util.ArrayList;
import java.util.List;


public class StringTest {

    public static void main(String[] args) {
        // one 和 two 编译成.class 文件后再用jd.gui 发现one ="abcde"
        String one = "a" + "b" + "c" + "d" + "e";

        String two = "abcde";

        //自己定义的变量在反编译之后,在JVM修改成 localList1
        //List<Integer>  ssssa = List.of(1,2,3,4);

        //自己定义的变量在反编译之后,在JVM修改成 localList2
        //List<Integer>  bbb = List.of(1,2,3,4);
    }
}
