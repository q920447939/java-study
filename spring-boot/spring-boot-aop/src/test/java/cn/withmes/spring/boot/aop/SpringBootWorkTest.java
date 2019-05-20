/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */
package cn.withmes.spring.boot.aop;

import cn.withmes.spring.boot.aop.work.FakerDataBase;
import cn.withmes.spring.boot.aop.work.Product;
import cn.withmes.spring.boot.aop.work.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ClassName: SpringBootWorkTest
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月14日
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootWorkTest {
    @Autowired
    ProductService productService;


    @Test
    public void addTest() {
        Product p = new Product();
        p.setProductName("香蕉榴莲");
        p.setProductPrice(50.3);
        productService.add(p);
        System.out.println(FakerDataBase.productMap);
    }
}
