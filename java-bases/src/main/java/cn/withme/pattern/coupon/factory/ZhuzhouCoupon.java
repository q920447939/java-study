package cn.withme.pattern.coupon.factory;

import cn.withme.pattern.coupon.factory.domain.Coupon;
import cn.withme.pattern.coupon.factory.domain.ZhuzhouCouponDomain;

/**
 * @author liming
 * @Description:
 * @date 2020年01月10日
 */
public  class ZhuzhouCoupon  extends CouponAbstractFactory{

     Coupon create() {
       return new ZhuzhouCouponDomain(2,"create ZhuzhouCouponDomain....");
    };
}
