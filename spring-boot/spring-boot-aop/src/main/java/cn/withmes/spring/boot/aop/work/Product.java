/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */
package cn.withmes.spring.boot.aop.work;

import lombok.Data;
import lombok.ToString;

/**
 * ClassName: Product
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
@Data
@ToString
public class Product {

    private Integer id;
    private String productName;
    private Double productPrice;

}
