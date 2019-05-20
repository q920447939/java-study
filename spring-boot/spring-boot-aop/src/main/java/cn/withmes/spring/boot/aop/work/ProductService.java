/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */
package cn.withmes.spring.boot.aop.work;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * ClassName: ProductService
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
@Service
public class ProductService {

    public int add (Product product) {
        int id = FakerDataBase.idAtom.getAndIncrement();
        System.out.println("进入新增方法,product.id:"+id);
        product.setId(id);
        FakerDataBase.productMap.put(id, product);
        return id;
    }
}
