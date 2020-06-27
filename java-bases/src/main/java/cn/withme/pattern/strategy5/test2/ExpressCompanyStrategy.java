/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2;

import cn.withme.pattern.strategy5.test1.domain.ExPressCompany;
import cn.withme.pattern.strategy5.test2.domain.BaseExPressCompany;

/**
 * ClassName: ExpressCompanyStrategy
 * @Description:
 * @author leegoo
 * @date 2020年06月26日
 */
public interface ExpressCompanyStrategy {
    BaseExPressCompany create(ExpressDelegate delegate);
}
