package cn.withme.pattern.strategy;

/**
 * @author liming
 * @Description:
 * @date 2019年11月22日
 */
public class ChinaDuck  extends Duck{

    private FlyBehavior flyBehavior;
    private  SoundBehavior soundBehavior;

    public ChinaDuck(FlyBehavior flyBehavior, SoundBehavior soundBehavior) {
        this.flyBehavior = flyBehavior;
        this.soundBehavior = soundBehavior;
    }

    public FlyBehavior getFlyBehavior() {
        return flyBehavior;
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public SoundBehavior getSoundBehavior() {
        return soundBehavior;
    }

    public void setSoundBehavior(SoundBehavior soundBehavior) {
        this.soundBehavior = soundBehavior;
    }
}
