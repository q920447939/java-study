/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test1;

import cn.withme.pattern.strategy5.test1.domain.ExPressCompany;
import cn.withme.pattern.strategy5.test1.domain.YuanTongExpressCompany;

public class YuanTongExpressCompanyStrategyImpl implements ExpressCompanyStrategy {
    @Override
    public ExPressCompany create(int piece) {
        ExPressCompany exPressCompany = new YuanTongExpressCompany(10, 2,piece);
        if (piece > 5) exPressCompany.setPrice(exPressCompany.getPrice() * piece * 0.5);
        return exPressCompany;
    }
}
