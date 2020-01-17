package cn.withme.pattern.coupon.factory;

import cn.withme.pattern.coupon.factory.domain.Coupon;
import cn.withme.pattern.coupon.factory.domain.DefaultCouponDomain;

/**
 * @author liming
 * @Description:
 * @date 2020年01月10日
 */
public  class DefaultCoupon extends CouponAbstractFactory{

     Coupon create (){
        return  new DefaultCouponDomain(1,"create DefaultCoupon....");
    };
}
