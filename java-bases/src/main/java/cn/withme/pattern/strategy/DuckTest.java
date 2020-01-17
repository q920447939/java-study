package cn.withme.pattern.strategy;

/**
 * @author liming
 * @Description:
 * @date 2019年11月22日
 */
public class DuckTest {

    public static void main(String[] args) {
        FlyBehavior flyBehavior = new QuickFlyImpl();
        Duck duck = new ChinaDuck(flyBehavior, new SoundBehavior() {
            @Override
            public String sound() {
                return null;
            }
        });
    }
}
