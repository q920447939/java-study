package cn.withme.pattern.observe;

import java.util.Observable;
import java.util.Observer;

/**
 * @author liming
 * @Description:
 * @date 2020年01月03日
 */
public class WeatherObserver extends Observable {

    private BeiJingDetectLocal beiJingDetectLocal;

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }
}
