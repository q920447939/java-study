package cn.withme.pattern.coupon.factory.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author liming
 * @Description:优惠券
 * @date 2020年01月10日
 */
@Setter
@Getter
public class Coupon implements Serializable {
    private Integer id;
    private String name;
}
