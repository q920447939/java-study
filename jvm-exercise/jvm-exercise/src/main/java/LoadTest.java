/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年02月08日
 */

/**
 * ClassName: LoadTest
 * @Description:
 * @author leegoo
 * @date 2023年02月08日
 */
public class LoadTest {
    public static  int VALUE = 1000;
    static {
        VALUE = 200;
    }

    public LoadTest(){
        System.out.println(VALUE);
    }


    public static void main(String[] args) {
        new LoadTest();
    }
}
