/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2;

import cn.withme.pattern.strategy5.test1.domain.ExPressCompany;
import cn.withme.pattern.strategy5.test1.domain.ShunFengExpressCompany;
import cn.withme.pattern.strategy5.test2.domain.BaseExPressCompany;
import cn.withme.pattern.strategy5.test2.domain.ShunFengExpressCompanyBaseExPressCompany;

/**
 * ClassName: ShunfengExpressCompanyStrategyImpl
 * @Description:
 * @author leegoo
 * @date 2020年06月26日
 */
public class ShunFengExpressCompanyStrategyImpl implements ExpressCompanyStrategy {
    @Override
    public BaseExPressCompany create(ExpressDelegate delegate) {
        BaseExPressCompany exPressCompany = new ShunFengExpressCompanyBaseExPressCompany(15, delegate.speed,delegate.piece);
        if (delegate.piece > 3) exPressCompany.setPrice(exPressCompany.getPrice() * delegate.piece * 0.8);
        return exPressCompany;
    }
}
