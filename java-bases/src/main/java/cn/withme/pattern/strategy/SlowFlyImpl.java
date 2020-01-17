package cn.withme.pattern.strategy;

/**
 * @author liming
 * @Description:
 * @date 2019年11月22日
 */
public class SlowFlyImpl implements  FlyBehavior
{
    @Override
    public String fly() {
        return "fly so slow....";
    }
}
