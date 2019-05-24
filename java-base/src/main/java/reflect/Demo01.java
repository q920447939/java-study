/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月21日
 */
package reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ClassName: Demo01
 * @Description:
 * @author leegoo
 * @date 2019年05月21日
 */
public class Demo01 {
    private volatile  Integer i = 0;

    public void writer(String s){

    }

    public Object writer2(String s)throws Exception{
        return 1;
    }

    public static void main(String[] args) {
        try {
            Class<?> clazz = Class.forName("reflect.Demo01");
            Method[] method=clazz.getDeclaredMethods();
            for(Method m:method){
                System.out.println(m.toString());
            }

            //获取 Person 类的所有成员属性信息
            Field[] field=clazz.getDeclaredFields();
            for(Field f:field){
                System.out.println(f.toString());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
