/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */
package debug;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DebugDemo
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
public class DebugDemo
{
    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        for (int i = 0; i < a.size(); i++) {
            System.out.println(i);
        }
    }
}
