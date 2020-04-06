/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年04月05日
 */
package demo;

import java.io.Serializable;

/**
 * ClassName: UserImpl
 * @Description:
 * @author leegoo
 * @date 2020年04月05日
 */
public class UserImpl2 implements  User, Serializable {
    @Override
    public String getName() {
        return "王大锤";
    }
}
