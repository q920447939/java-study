/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月16日
 */
package cn.withmes.spring.boot.source.find;

/**
 * ClassName: Monkey
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月16日
 */
@MyAnnotation(name = "oneMonkey")
public class Monkey {

    @Override
    public String toString() {
        System.err.println("class Monkey, method toString");
        return null;
    }


}

