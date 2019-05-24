/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月22日
 */
package cn.withmes.springboot.mybatis.interceptor.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * ClassName: PageBase
 * @Description:
 * @author leegoo
 * @date 2019年05月22日
 */
@Data
@ToString
public class PageBase {

    protected Integer StartPage;
    protected Integer endPage;

}
