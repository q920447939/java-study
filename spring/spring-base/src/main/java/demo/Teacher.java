/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年04月23日
 */
package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ClassName: Teacher
 *
 * @author leegoo
 * @Description:
 * @date 2021年04月23日
 */
@Component(value = "teacher")
public class Teacher {
    @Value("${name}")
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
