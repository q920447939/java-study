/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月28日
 */
package cn.withmes.spring.springboot.action.pojo;

import cn.withmes.spring.springboot.action.service.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName: ExPlorer
 * @Description:
 * @author leegoo
 * @date 2019年06月28日
 */
@Component
public class ExPlorer {

    private  final Task task;


    public ExPlorer(Task task) {
        this.task = task;
    }

    public void work () {
        System.out.println("探险家开始工作,遇见特殊情况....");
        System.out.println(task.execute());
        System.out.println("结束");
    }
}
