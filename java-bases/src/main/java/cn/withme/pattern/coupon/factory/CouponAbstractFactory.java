package cn.withme.pattern.coupon.factory;

import cn.withme.pattern.coupon.factory.domain.Coupon;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liming
 * @Description:
 * @date 2020年01月10日
 */
public abstract class CouponAbstractFactory {

    private  static Map<String,CouponAbstractFactory> couponMap = new HashMap<>();

    static {
        couponMap.put("default",new DefaultCoupon());
        couponMap.put("zhuzhou",new ZhuzhouCoupon());
    }

    public  Coupon chose(String type){
        CouponAbstractFactory factory = couponMap.get(type);
        return factory.create();
    }

     abstract Coupon create();
}
