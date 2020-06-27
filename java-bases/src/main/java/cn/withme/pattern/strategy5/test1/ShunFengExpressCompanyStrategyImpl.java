/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test1;

import cn.withme.pattern.strategy5.test1.domain.ExPressCompany;
import cn.withme.pattern.strategy5.test1.domain.ShunFengExpressCompany;

/**
 * ClassName: ShunfengExpressCompanyStrategyImpl
 * @Description:
 * @author leegoo
 * @date 2020年06月26日
 */
public class ShunFengExpressCompanyStrategyImpl implements  ExpressCompanyStrategy{
    @Override
    public ExPressCompany create(int piece) {
        ExPressCompany exPressCompany = new ShunFengExpressCompany(15, 1,piece);
        if (piece > 3) exPressCompany.setPrice(exPressCompany.getPrice() * piece * 0.8);
        return exPressCompany;
    }
}
