package cn.withmes.spring.boot.custom;

import cn.withmes.spring.boot.custom.annotation.StartApplication;

@StartApplication() //启动注解 ,此处只是加一个注解.没有做扫描包的处理
public class SpringBootCustomApplication {

    public static void main(String[] args) {
        //spring boot 启动方式
        MyApplication.run();

        //测试是否注入
        MyApplication myApplication = new MyApplication();
        UserController controller = myApplication.getBean("userController", UserController.class);
        controller.getId("1");
    }
}
