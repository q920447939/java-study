package cn.withme.pattern.coupon.factor1;

import cn.withme.pattern.coupon.factory.domain.Coupon;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liming
 * @Description:
 * @date 2020年01月10日
 */
public abstract class CouponAbstractFactory {

    private Coupon create (String type){
        if (StringUtils.isBlank(type)){
        }
        return  null;
    };


}
