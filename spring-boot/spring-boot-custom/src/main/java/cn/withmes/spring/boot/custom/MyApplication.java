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
