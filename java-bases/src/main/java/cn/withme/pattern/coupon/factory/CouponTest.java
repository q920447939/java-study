package cn.withme.pattern.coupon.factory;

import cn.withme.pattern.coupon.factory.domain.Coupon;

/**
 * @className: CouponTest
 * @description: TODO 类描述
 * @author: liming
 * @date: 2020/1/14
 **/
public class CouponTest {
    public static void main(String[] args) {
        CouponAbstractFactory factory = new DefaultCoupon();
        Coupon coupon = factory.chose("zhuzhou");
        System.out.println(coupon);
    }
}
