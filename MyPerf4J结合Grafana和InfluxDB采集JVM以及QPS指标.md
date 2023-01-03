# MyPerf4J结合Grafana和InfluxDB采集JVM以及QPS指标

## 背景

​	需要采集现场`java`程序运行的状态数据(包括`JVM`指标以及`QPS,RPS`指标等)。需要采集的方式尽可能轻量化

​    结合实际情况采用`MyPerf4J`作为`Java`探针，`InfluxDB`作为数据存储端，`Grafana`作为数据展示端



## 展示

​     先看结果：![image-20221229150136995](F:\liming\work_space\my_work_space\java-study\img\myperf4j\image-20221229150136995.png)

## 安装

1. JDK = 1.8 ，如果采用JDK17程序会起不来(起不来指的是采用`MyPerf4J`作为`Java`探针的情况下)

2. 系统 =  `CentOS7`

3. InfluxDB = 1.7.8 ，此处采用离线安装方式(注意InfluxDB 不能是2.X的版本，`MyPerf4J`不适配)
   
   1. 下载`InfluxDB ` `tar`包,下载地址：
   
   2. 解压`tar xf influxdb-1.7.8_linux_amd64.tar.gz`
   
   3. 启动 
   
      1. 我们到安装软件的usr/bin目录下，执行命令` ./influxd ` 前台启动 ；如图所示就代表启动成功
   
         ![image-20221229172337822](F:\liming\work_space\my_work_space\java-study\img\myperf4j\image-20221229172337822.png)

4. 安装`grafana`

   1. 下载`grafana-enterprise-9.3.2.linux-amd64.tar.gz`

   2. 解压`tar -zxf grafana-enterprise-9.3.2.linux-amd64.tar.gz`

   3. 进入`grafana-9.3.2/bin`目录

   4. 执行命令`./grafana-server &`，启动成功如图所示

      ![image-20221229172927617](F:\liming\work_space\my_work_space\java-study\img\myperf4j\image-20221229172927617.png)



4. 下载`MyPerf4J-ASM-3.2.0-SNAPSHOT.zip` ，原项目地址`https://github.com/LinShunKang/MyPerf4J`

   1. 上传至`CentOS7`服务器并解压`MyPerf4J-ASM-3.2.0-SNAPSHOT.zip` 

   2. 修改解压后的`MyPerf4j.properties`文件，主要**注意**配置文件中的配置项`metrics.exporter ;filter.packages.include;influxdb`，我修改的文件如下：

```properties
# MyPerf4J 所有配置请参考：https://github.com/LinShunKang/MyPerf4J/wiki/%E9%85%8D%E7%BD%AE

# 配置监控应用的名称
app_name = MyApp

debug = true

###############################################################################
#                           Metrics Configuration                             #
###############################################################################

# 配置 MetricsExporter 类型
#       log.stdout:     以标准格式化结构输出到 stdout.log
#       http.influxdb:  以 InfluxDB LineProtocol 格式发送至 InfluxDB server，需要另行增加 influxdb 的配置
metrics.exporter = http.influxdb

#MetricsProcessor类型，0:以标准格式化结构输出到stdout.log 1:以标准格式化结构输出到磁盘  2:以InfluxDB LineProtocol格式输出到磁盘
MetricsProcessorType=2

# 配置各项监控指标日志的文件路径
# 如果 metrics.exporter 配置为 log.influxdb，建议把所有的 metrics.log.* 路径配置成一样以方便 Telegraf 收集
# 这里我发现如果 metrics.exporter 采用 http.influxdb方式，这块的配置可以不需要配置
metrics.log.method = /home/tzdata/application/MyPerf4j/metrics.log
metrics.log.class_loading = /home/tzdata/application/MyPerf4j/metrics.log
metrics.log.gc = /home/tzdata/application/MyPerf4j/metrics.log
metrics.log.memory = /home/tzdata/application/MyPerf4j/metrics.log
metrics.log.buff_pool = /home/tzdata/application/MyPerf4j/metrics.log
metrics.log.thread = /home/tzdata/application/MyPerf4j/metrics.log
metrics.log.file_desc = /home/tzdata/application/MyPerf4j/metrics.log
metrics.log.compilation = /home/tzdata/application/MyPerf4j/metrics.log


###############################################################################
#                           Filter Configuration                              #
###############################################################################

# 配置需要监控的package，可配置多个，用英文';'分隔
#   com.demo.p1 代表包含以 com.demo.p1 为前缀的所有包和类
#   [] 表示集合的概念：例如，com.demo.[p1,p2,p3] 代表包含以 com.demo.p1、com.demo.p2 和 com.demo.p3 为前缀的所
有包和类，等价于 com.demo.p1;com.demo.p2;com.demo.p3
#   * 表示通配符：可以指代零个或多个字符，例如，com.*.demo.*
filter.packages.include = com.*;

# 配置不需要监控的package，可配置多个，用英文';'分隔
#filter.packages.exclude = cn.perf4j.demo.dao.DemoDAOImpl


influxdb.host=127.0.0.1
influxdb.port=8086
influxdb.database=MyPerf4J
influxdb.username=admin
influxdb.password=admin
influxdb.conn_timeout=3000
influxdb.read_timeout=5000
```



## 编写SpringBoot项目

1. 代码部分

   1. 代码比较简单，只有一个功能：每隔一秒钟打印一下时间。

```java
@SpringBootApplication
@EnableScheduling
public class SpringBootDemoApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringBootDemoApplication.class, args);
        System.out.println("程序启动....");
        Thread.sleep(Integer.MAX_VALUE);

    }


    @Scheduled(fixedDelay = 1,timeUnit = TimeUnit.SECONDS)
    public void  aa () {
        System.out.println("当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}
```

2. 启动应用
   
   2.1. 在启动的时候增加命令
```java
在 JVM 启动参数里加上以下两个参数
-javaagent:/path/to/MyPerf4J-ASM.jar
-DMyPerf4JPropFile=/path/to/MyPerf4J.properties
形如：java -javaagent:/path/to/MyPerf4J-ASM.jar -DMyPerf4JPropFile=/path/to/MyPerf4J.properties -jar yourApp.jar

```
   2.2. 启动成功后会在控制台打印，注意控制台打印了`status=HttpRespStatus{code=200, phrase='OK'`,如果打印`xxx 401`，那么就是`influxDB`的配置信息错误或者`influxDB`版本是`2.x`的版本

```java
2022-12-30 09:39:32.838 [MyPerf4J] INFO [main] Thanks sincerely for using MyPerf4J.
2022-12-30 09:39:32.868 [MyPerf4J] INFO [main] http.server.port is not configured, so use '2048,2000,2040' as default.
2022-12-30 09:39:32.870 [MyPerf4J] INFO [main] ClassLevelMapping is blank, so use default mappings.
2022-12-30 09:39:32.951 [MyPerf4J] INFO [main] InfluxDbClient create database 'MyPerf4J' response.status=HttpRespStatus{code=200, phrase='OK'}
2022-12-30 09:39:32.973 [MyPerf4J] INFO [main] Use 2048 as HttpServer port.
2022-12-30 09:39:33.102 [MyPerf4J] INFO [main] 
    __  ___      ____            ______ __      __
   /  |/  /_  __/ __ \___  _____/ __/ // /     / /
  / /|_/ / / / / /_/ / _ \/ ___/ /_/ // /___  / / 
 / /  / / /_/ / ____/  __/ /  / __/__  __/ /_/ /  
/_/  /_/\__, /_/    \___/_/  /_/    /_/  \____/   
       /____/                                     v3.2.0-SNAPSHOT
```



## `grafana`配置`InfluxDB`数据库信息

1. 登录`grafana`网页地址，地址是部署的`grafana`服务`IP:3000`端口 ；例如：`http://192.168.200.166:3000/`

2. 默认登录账号密码是：`admin/admin`

3. 打开`grafana`的`DB`配置，点击`Add data source`

   ![image-20221230095822756](F:\liming\work_space\my_work_space\java-study\img\myperf4j\image-20221230095822756.png)
4. 选择`InfluxDB`
![image-20221230095905576](F:\liming\work_space\my_work_space\java-study\img\myperf4j\image-20221230095905576.png)


5. 填写`Http URL`(只需要把IP改成`InfluxDB`部署的`IP`即可)
![image-20221230100006290](F:\liming\work_space\my_work_space\java-study\img\myperf4j\image-20221230100006290.png)


6. 填写`InfluxDB`对应的配置信息，然后点击`Save & test`,连接成功会提示绿色的信息
![image-20221230100206672](F:\liming\work_space\my_work_space\java-study\img\myperf4j\image-20221230100206672.png)



7. 点击`Dashboards`，点击`Import`
![image-20221230100347325](F:\liming\work_space\my_work_space\java-study\img\myperf4j\image-20221230100347325.png)



8. 点击`Upload JSON file` ,分别上传`myperf4j-influxdb-jvm_rev6.json`、和`myperf4j-influxdb-method_rev6.json`






9. 点击`Dashboards`，就能看到上传的`MyPerf4J-InfluxDBv1.x-JVM`、和`MyPerf4J-InfluxDBv1.x-Method`




![image-20221230100729260](F:\liming\work_space\my_work_space\java-study\img\myperf4j\image-20221230100729260.png)




10. 随意点击一个图表就能查看监控指标信息，图在展示目录
