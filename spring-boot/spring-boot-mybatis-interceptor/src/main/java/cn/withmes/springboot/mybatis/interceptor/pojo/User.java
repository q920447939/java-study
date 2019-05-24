/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月22日
 */
package cn.withmes.springboot.mybatis.interceptor.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * ClassName: User
 * @Description:
 * @author leegoo
 * @date 2019年05月22日
 */
@Data
public class User extends  PageBase{
    private Long id;
    private String name;
    private Integer age;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", StartPage=" + StartPage +
                ", endPage=" + endPage +
                '}';
    }
}
