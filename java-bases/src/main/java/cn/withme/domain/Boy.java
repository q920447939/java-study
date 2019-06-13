/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月11日
 */
package cn.withme.domain;

import lombok.Data;

/**
 * ClassName: Boy
 * @Description:
 * @author leegoo
 * @date 2019年06月11日
 */

@Data
public class Boy {


    private  int no ;
    private Boy next ;

    public Boy(int no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "Boy{" +
                "no=" + no +
                ", next=" + next +
                '}';
    }
}
