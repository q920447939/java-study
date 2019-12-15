# 纯手写SpringBoot+Spring MVC

我们知道springBoot相比于Spring的话省略了很多配置，而且可以通过`main`方法的形式启动spring boot web项目。 (源码地址:https://github.com/q920447939/java-study/tree/master/spring-boot/spring-boot-custom)

那么我们今天也来实现一个自定义的SpringBoot +Spring MVC

当然,了解过Spring的童靴肯定知道Spring其实是依赖servlet做了一层处理而已。所以我们在`pom.xml`必须要依赖

```xml
 <dependency>
     <groupId>javax.servlet</groupId>
     <artifactId>servlet-api</artifactId>
</dependency>
```



#### 先复习一下spring  创建Bean的大致流程

1. 定位
2. 加载
3. 注册



### 那么现在的开始

1. 先创建一个SpringBoot项目(看看SpirngBoot和Spring的不同)
```java
package cn.withmes.spring.boot.custom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCustomApplication.class, args);
    }

}

```

在主方法上面多了一个SpringBoot的注解,那么我们也照护画瓢弄一个自己的启动注解

```java
package cn.withmes.spring.boot.custom.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @param: 
 * @return: 
 * @auther: liming
 * @date: 12/15/2019 3:18 PM
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartApplication {

    String scanPackage() default "";
}

```





```java
/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年12月15日
 */
package cn.withmes.spring.boot.custom;

/**
 * ClassName: MyApplication
 * @Description:
 * @author leegoo
 * @date 2019年12月15日
 */
public class MyApplication {

    public static void run  () {

    }
}

```



```java

package cn.withmes.spring.boot.custom;

import cn.withmes.spring.boot.custom.annotation.StartApplication;

@StartApplication()
public class SpringBootCustomApplication {

    public static void main(String[] args) {
        MyApplication.run();
    }
}

```



当前的目录结构为:

```
D:.
│  pom.xml
│  tree.txt
│  
└─src
    └─main
        ├─java
        │  └─cn
        │      └─withmes
        │          └─spring
        │              └─boot
        │                  └─custom
        │                      │  MyApplication.java
        │                      │  SpringBootCustomApplication.java
        │                      │  
        │                      └─annotation
        │                              StartApplication.java
        │                              
        └─resources
                application.properties
                
```





做了一个简单的bean工厂,以及自动注入 

```java
/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年12月15日
 */
package cn.withmes.spring.boot.custom;

import cn.withmes.spring.boot.custom.annotation.MyController;
import cn.withmes.spring.boot.custom.annotation.MyResource;
import cn.withmes.spring.boot.custom.annotation.MyService;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: MyApplication
 *
 * @author leegoo
 * @Description:
 * @date 2019年12月15日
 */
public class MyApplication {

    //存储bean,
    private static Map<String, Object> ioc = new ConcurrentHashMap<>();

    // 将所有的类全部加载进来
    private static List<String> cacheList = new ArrayList<>();

    public    <T> T  getBean (String beanName,Class<T> clz){
        return (T) ioc.get(beanName);
    }

    public static void  run() {
        //定位
        doLocation("");
        //加载
        doLoad();
        // 注册
        doAutoInject();
    }



    private static void doLocation(String pt) {
        String path = StringUtils.isBlank(pt) ? MyApplication.class.getResource(pt).getPath() : pt;
        File files = new File(path);
        File[] listFiles = files.listFiles();

        for (File file : listFiles) {
            if (file.isDirectory()) {
                doLocation(file.getPath());
            } else {
                if (file.getPath().contains("annotation")) continue;

                String[] targets = file.getPath().split("\\\\target\\\\classes\\\\");
                String className = targets[1].replaceAll("\\\\", ".").replace(".class", "");
                //将包路径下面的.java文件全部加载到cacheList中.
                cacheList.add(className);
            }
        }

    }


    private static void doLoad() {
        if (cacheList.size() <= 0) return;

        cacheList.forEach(k->{
            try {
                Class<?> cls = Class.forName(k);
                MyController controller = cls.getAnnotation(MyController.class);
                if (null != controller) {
                    ioc.put(lowerFirstChar(cls.getSimpleName()),cls.newInstance());
                    return;
                }
                MyService service = cls.getAnnotation(MyService.class);
                if (null != service){
                    Class<?>[] interfaces = cls.getInterfaces();
                    if (null == interfaces || interfaces.length  <= 0) return;
                    //此处只取第一个接口
                    Class<?> firstInterface = interfaces[0];
                    ioc.put(lowerFirstChar(firstInterface.getSimpleName()),cls.newInstance());
                }
            } catch (ClassNotFoundException |IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }


    private static void doAutoInject() {
        if (ioc.size() <= 0) return;

        //循环cacheMap ,如果类里面的属性包含 @MyResource ,那么自动注入对象
        ioc.forEach((k, v) -> {
            Field[] fields = v.getClass().getDeclaredFields();
            if (fields.length <= 0 ) return;

            for (Field fd : fields) {
                MyResource myResource = fd.getAnnotation(MyResource.class);
                if (null == myResource) continue;

                //设置访问权限
                fd.setAccessible(true);
                try {
                    //给带有@MyResource,set具体的值
                    fd.set(v, ioc.get(fd.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 将首字母小写
     * @param str
     * @return
     */
    private static String lowerFirstChar(String str){
        char [] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}

```



controller

```java
/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年12月15日
 */
package cn.withmes.spring.boot.custom;

import cn.withmes.spring.boot.custom.annotation.MyController;
import cn.withmes.spring.boot.custom.annotation.MyResource;

/**
 * ClassName: UserController
 * @Description:
 * @author leegoo
 * @date 2019年12月15日
 */
@MyController
public class UserController {

    @MyResource
    private UserService userService;

    public void getId (String id ) {
        String name = userService.getName(id);
        System.out.println("name:"+name);
    }
}

```





serivce

```java
/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年12月15日
 */
package cn.withmes.spring.boot.custom;

/**
 * ClassName: UserService
 * @Description:
 * @author leegoo
 * @date 2019年12月15日
 */
public interface UserService {

    String getName (String id );
}

```





serviceimpl

```java

/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年12月15日
 */
package cn.withmes.spring.boot.custom;

import cn.withmes.spring.boot.custom.annotation.MyService;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: UserServiceImpl
 * @Description:
 * @author leegoo
 * @date 2019年12月15日
 */
@MyService
public class UserServiceImpl implements  UserService {

    private static Map<String,String> userMap = new HashMap<>();

    static {
        userMap.put("1", "张三");
        userMap.put("2", "李四");
        userMap.put("3", "王五");
    }

    @Override
    public String getName(String id) {
        return null == id  || !userMap.containsKey(id) ? "没有此用户":
                userMap.get(id) ;
    }
}

```



注解类省略



开始测试

```java
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
        controller.getId("1"); //  name:张三   自动注入成功
    }
}

```

