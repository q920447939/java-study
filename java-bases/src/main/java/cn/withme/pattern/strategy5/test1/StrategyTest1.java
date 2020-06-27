/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test1;

import cn.withme.pattern.strategy5.test1.domain.ExPressCompany;
import cn.withme.pattern.strategy5.test1.domain.ShunFengExpressCompany;
import cn.withme.pattern.strategy5.test1.domain.YuanTongExpressCompany;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: StrategyTest1
 *
 * @author leegoo
 * @Description:
 * @date 2020年06月26日
 */
public class StrategyTest1 {

    private static final Map<Integer,ExpressCompanyStrategy> EXPRESS_COMPANY_STRATEGY_MAP = new HashMap<>();
    static {
        EXPRESS_COMPANY_STRATEGY_MAP.put(1, new ShunFengExpressCompanyStrategyImpl());
        EXPRESS_COMPANY_STRATEGY_MAP.put(2, new YuanTongExpressCompanyStrategyImpl());
    }
    public static void main(String[] args) {
        final  int companyType = 1;
        ExPressCompany exPressCompany = create(companyType, 4);
        System.err.println(exPressCompany);
        //策略模式版本
        ExPressCompany shunfStrategy = create2(EXPRESS_COMPANY_STRATEGY_MAP.get(companyType), 4);
        System.err.println(shunfStrategy);
    }

    /**
     * @param companyType 1 = 顺丰, 2 = 圆通
     * @param piece 件数
     * @description:
     * @return: cn.withme.pattern.strategy5.test1.domain.ExPressCompany
     * @date: 6/26/2020 4:02 PM
     */
    public static ExPressCompany create(int companyType, int piece) {
        ExPressCompany exPressCompany = new ExPressCompany();
        if (companyType <= 0 || piece <= 0) return exPressCompany;
        if (1 == companyType) exPressCompany = new ShunFengExpressCompany(15, 1,piece);
        else if (2 == companyType) exPressCompany = new YuanTongExpressCompany(10, 2,piece);

        //如果是顺丰,并且件数大于3 ,那么 价格打八折
        if (1 == companyType && piece > 3) exPressCompany.setPrice(exPressCompany.getPrice() * piece * 0.8);
            //如果是圆通,并且件数大于5 ,那么 价格打五折
        else if (2 == companyType && piece > 5) exPressCompany.setPrice(exPressCompany.getPrice() * piece * 0.5);

        //后续可能还有很多业务逻辑,更多的if else

        return exPressCompany;
    }


    public static ExPressCompany create2(ExpressCompanyStrategy expressCompanyStrategy, int piece) {
        return expressCompanyStrategy.create(piece);
    }
}
