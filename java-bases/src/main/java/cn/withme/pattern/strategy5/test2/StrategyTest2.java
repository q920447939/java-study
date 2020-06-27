/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2;


import cn.withme.pattern.strategy5.test2.domain.BaseExPressCompany;
import cn.withme.pattern.strategy5.test2.domain.ShunFengExpressCompanyBaseExPressCompany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: StrategyTest1
 *
 * @author leegoo
 * @Description:
 * @date 2020年06月26日
 */
public class StrategyTest2 {

    private static final Map<Integer, ExpressCompanyStrategy> EXPRESS_COMPANY_STRATEGY_MAP = new HashMap<>();

    static {
        EXPRESS_COMPANY_STRATEGY_MAP.put(1, new ShunFengExpressCompanyStrategyImpl());
        EXPRESS_COMPANY_STRATEGY_MAP.put(2, new YuanTongExpressCompanyStrategyImpl());
    }

    public static void main(String[] args) {
        final int companyType = 1;
        ExpressCompanyStrategy expressCompanyStrategy = EXPRESS_COMPANY_STRATEGY_MAP.get(companyType);
        List<BaseExPressCompany> companyList = generator(expressCompanyStrategy, 2,5, 1, 6);
        System.out.println(companyList);
//        List<BaseExPressCompany> soft = sortBySpeed(companyList,new ShunFengExpressCompanyBaseExPressCompany());
        /*List<BaseExPressCompany> soft = sortBySpeed(companyList,(t1,t2)->{
            if (t1.getPrice() < t2.getPrice()) return  1;
            else  if  (t1.getPrice() > t2.getPrice()) return  -1;
            return 0;
        });*/
        System.out.println("============");
        //根据泛型类型排序
        Sort<BaseExPressCompany> soft = new MySort<>();
        soft.sortBySpeed(BaseExPressCompany.class,companyList,(t1,t2)->{
            if (t1.getPrice() < t2.getPrice()) return  1;
            else  if  (t1.getPrice() > t2.getPrice()) return  -1;
            return 0;
        });
        System.err.println(soft);
    }


    /**
     * @description: 生成数据
     * @param expressCompanyStrategy
     * @param piece
     * @return: java.util.List<cn.withme.pattern.strategy5.test2.domain.BaseExPressCompany>
     * @author: liming
     * @date: 6/26/2020 7:17 PM
     */
    public static List<BaseExPressCompany> generator(ExpressCompanyStrategy expressCompanyStrategy, int... piece) {
        List<BaseExPressCompany> result = new ArrayList<>(piece.length);
        ExpressDelegate delegate = null;
        List<Integer> speedList = new ArrayList<>();
        speedList.add(2);
        speedList.add(1);
        speedList.add(5);
        speedList.add(3);
        for (int value : piece) {
            int speedTmp = 0;
            if (!speedList.isEmpty()) speedTmp  = speedList.get(0);
            delegate = new ExpressDelegate(value, speedTmp);
            result.add(expressCompanyStrategy.create(delegate));
            speedList.remove(0);
        }
        return result;
    }

    /**
     * @description: 根据发送速度排序
     * @param companyList
     * @return: java.util.List<cn.withme.pattern.strategy5.test2.domain.BaseExPressCompany>
     * @author: liming
     * @date: 6/26/2020 7:17 PM
     */
    public static List<BaseExPressCompany> sortBySpeed(List<BaseExPressCompany> companyList,MyComparator<BaseExPressCompany> comparator) {
        List<BaseExPressCompany> result = new ArrayList<>(companyList.size());
        BaseExPressCompany[] array = companyList.toArray(new BaseExPressCompany[companyList.size()]);
        for (int i = 0; i < array.length; i++) {
            for (int j = i; j < array.length; j++) {
                if (comparator.sort(array[j],array[i])>= 1){
                    swap(array,i,j);
                }
            }
        }
        result = Arrays.asList(array);
        return result;
    }

    /**
     * @description: 将数组中的i下标数据与j下标数据进行交换
     * @param arr
     * @param i
     * @param j
     * @return: void
     * @author: liming
     * @date: 6/26/2020 7:16 PM
     */
    public static  void swap (BaseExPressCompany [] arr ,int i ,int j){
        BaseExPressCompany tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
