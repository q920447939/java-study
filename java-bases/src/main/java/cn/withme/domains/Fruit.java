/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月22日
 */
package cn.withme.domains;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ClassName: Fruit
 * @Description:
 * @author leegoo
 * @date 2020年06月22日
 */

@Getter
@Setter
public class Fruit<T> {
    private String msg ;
    private boolean flag;
    T data;

    public Fruit() {

    }

    public  void succ (T data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "msg='" + msg + '\'' +
                ", flag=" + flag +
                ", data=" + data +
                '}';
    }
}
