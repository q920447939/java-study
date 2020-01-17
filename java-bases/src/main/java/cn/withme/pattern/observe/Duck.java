package cn.withme.pattern.observe;

/**
 * @author liming
 * @Description:
 * @date 2019年12月12日
 */
public class Duck  extends Zoo implements Observe {

    @Override
    public String say() {
        return " i am duck";
    }



}
