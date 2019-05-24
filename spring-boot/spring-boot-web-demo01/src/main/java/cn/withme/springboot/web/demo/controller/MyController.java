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

/**
 * ClassName: MyController
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月22日
 */
@RestController
@RequestMapping("/test")
public class MyController {

    private Student student;

    @GetMapping("/stu")
    public Student getStudentMessage() {
        System.out.println(student);
        return student;
    }

}
