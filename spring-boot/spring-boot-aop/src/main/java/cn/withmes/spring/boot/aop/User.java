/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月13日
 */
package cn.withmes.spring.boot.aop;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: User
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月13日
 */
@Data
@ToString
@Builder
public class User {
    private Integer id;
    private String name;
    public  static List<User> userList = new ArrayList<>();
}
