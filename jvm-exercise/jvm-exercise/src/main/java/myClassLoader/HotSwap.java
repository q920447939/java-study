/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年02月15日
 */
package myClassLoader;

import java.lang.reflect.Method;

/**
 * ClassName: HotSwap
 * @Description:
 * @author leegoo
 * @date 2023年02月15日
 */
// 使用类加载器加载类
public class HotSwap {
    public static void main(String[] args) throws Exception {
        System.out.println(HotSwap.class.getClassLoader());
    }
}
