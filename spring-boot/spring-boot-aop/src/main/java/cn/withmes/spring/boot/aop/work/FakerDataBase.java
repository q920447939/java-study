/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */
package cn.withmes.spring.boot.aop.work;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: FakerDataBase
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
public class FakerDataBase {
    public static Map<Integer,Product> productMap = new ConcurrentHashMap<>();
    public static volatile AtomicInteger idAtom = new AtomicInteger(1);
}
