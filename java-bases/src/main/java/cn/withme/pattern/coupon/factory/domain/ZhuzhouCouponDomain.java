package cn.withme.pattern.coupon.factory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liming
 * @Description:优惠券
 * @date 2020年01月10日
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class ZhuzhouCouponDomain extends Coupon  implements Serializable {
    private Integer id;
    private String name;
}
