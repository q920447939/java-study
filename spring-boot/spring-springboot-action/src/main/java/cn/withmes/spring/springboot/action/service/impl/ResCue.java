/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月28日
 */
package cn.withmes.spring.springboot.action.service.impl;

import cn.withmes.spring.springboot.action.service.Task;
import org.springframework.stereotype.Service;

/**
 * ClassName: ResCue
 *
 * @author leegoo
 * @Description:抢救
 * @date 2019年06月28日
 */
@Service
public class ResCue implements Task {


    @Override
    public String execute() {
        return "探险家救了几个人";
    }
}
