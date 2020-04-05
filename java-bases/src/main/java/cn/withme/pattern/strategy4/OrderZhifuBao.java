/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月28日
 */
package cn.withme.pattern.strategy4;

import lombok.Data;

/**
 * ClassName: OrderDomain
 * @Description:
 * @author leegoo
 * @date 2020年03月28日
 */
@Data
public class OrderZhifuBao {
    private BankCodeEnum sourceChannelCode;

    private String customerId;

    private String customerName;

    private String secureKey;

}
