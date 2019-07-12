/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月22日
 */
package cn.withme.springboot.web.demo.controller;

import cn.withme.springboot.web.demo.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: MyController
 *
 * @author leegoo
 * @Description: 测试spring boot优雅停止
 * @date 2019年05月22日
 */
@RestController
@RequestMapping("/test")
public class MyController {

    private Student student;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @GetMapping("/stu")
    public String  getStudentMessage(int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            executorService.execute(()->{
                try {
                    Thread.sleep(10000);
                    System.out.println("正在睡眠....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        return "完成";
    }


    @PreDestroy
    public void Sss () {
        executorService.shutdown();
        System.out.println("关闭...");
    }

}
