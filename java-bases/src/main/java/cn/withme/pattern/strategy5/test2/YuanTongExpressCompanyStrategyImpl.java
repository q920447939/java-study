/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2;

import cn.withme.pattern.strategy5.test1.domain.ExPressCompany;
import cn.withme.pattern.strategy5.test1.domain.YuanTongExpressCompany;
import cn.withme.pattern.strategy5.test2.domain.BaseExPressCompany;
import cn.withme.pattern.strategy5.test2.domain.YuanTongExpressCompanyBaseExPressCompany;

public class YuanTongExpressCompanyStrategyImpl implements ExpressCompanyStrategy {
    @Override
    public BaseExPressCompany create(ExpressDelegate delegate) {
        BaseExPressCompany exPressCompany = new YuanTongExpressCompanyBaseExPressCompany(10, delegate.speed, delegate.piece);
        if (delegate.piece > 5) exPressCompany.setPrice(exPressCompany.getPrice() * delegate.piece * 0.5);
        return exPressCompany;
    }
}
