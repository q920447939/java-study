####<a name="test">标题一</a>


## **多线程 部分**

[java 死锁的一个例子](java-bases/src/main/java/cn/withme/thread/DealLock.java)

## **spring 部分**

[spring](./spring)




## **Java知识点整理**

[Java知识点整理](./Java知识点整理.xmind)


## **spring boot 部分**
[简单的spring boot  security 实现](./spring-boot/spring-boot-security)

[java SPI](spring-boot/spring-boot-java-spi/src/main/java/cn/withmes/spi/SpiTest.java)

## **spring cloud 部分**

1. [eureka服务注册与服务发现 + feign](spring-cloud/spring-cloud-eureka)

2. [ribbon源码研究](spring-cloud/spring-cloud-simulate-ribbon-service/src/main/java/cn/withmes/spring/cloud/simulate/ribbon/service/SimulateApplication.java)

3. feign源码研究
   1. [feignclient+Hystrix](spring-cloud/spring-cloud-fegin/spring-cloud-open-fegin-client/src/main/java/cn/withmes/springcloud/open/fegin/client/FeginClient.java)
   2. [feignserver+rabbion](spring-cloud/spring-cloud-fegin/spring-cloud-open-fegin-server/src/main/java/cn/withmes/spring/cloud/open/fegin/server/FeginServer.java)
   
   
## **test**

## **Docker 部分**
1. [将spring-cloud项目打包成docker镜像并启动](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-server01/spring-cloud-eureka-server)

2. 将spring-cloud项目打包成docker镜像并启动

   - pom.xml 里面加入如下配置:

     ```xml
     
         <build>
             <finalName>spring-cloud-eureka-server01-service</finalName>
             
             <plugins>
                 <plugin>
                     <groupId>com.spotify</groupId>
                     <artifactId>docker-maven-plugin</artifactId>
                     <version>0.4.13</version>
                     <configuration>
                         <!-- leegoo0820/spring-cloud-eureka-server01-service(name):0.0.1(tag)-->
                         <imageName>leegoo0820/spring-cloud-eureka-server01-service:0.0.1</imageName>
                         <!-- 基础镜像 -->
                         <baseImage>java</baseImage>
                         <entryPoint>["java","-jar","/${project.build.finalName}.jar"]</entryPoint>
                         <resources>
                             <resource>
                                 <targetPath>/</targetPath>
                                 <!-- 目录地址,也就是target生成的地址 -->
                                 <directory>${project.build.directory}</directory>
                                 <!--需要复制的jar包 -->
                                 <include>${project.build.finalName}.jar</include>
                             </resource>
                         </resources>
                     </configuration>
                 </plugin>
             </plugins>
         </build>
     ```

   - 将项目文件传到linux 服务器:

   - 使用maven命令进行打包(需要提前配置[maven(3.5版本已经失效,需要自己去apache官网查看最新的下载地址)](<https://www.cnblogs.com/jimisun/p/8054819.html>) 环境,和[java](<https://www.cnblogs.com/xiaohao95/p/7615248.html>)环境才能进行打包):

     ```shell
     mvn clean package docker:build
     ```

   - 然后使用`docker images` 会看到 `spring-cloud-eureka-server01-service` 镜像

   - 随后启动 

     ```shell
     [root@localhost spring-cloud-eureka-server]# docker run -p 8761:8761 --name my_eureka leegoo0820/spring-cloud-eureka-server01-service:0.0.1
     ```


   - 如果希望只执行`maven clean package` 达到 `mvn clean package docker:build`的效果 ,那么可以在`pom.xml`文件里面增加如下配置,简单理解为就是配置了`execution`后,会执行对应的命令,当前命令是`build`

     ```xml
      <build>
             <finalName>spring-cloud-eureka-server01-service</finalName>
     
             <plugins>
                 <plugin>
                     <groupId>com.spotify</groupId>
                     <artifactId>docker-maven-plugin</artifactId>
                     <version>0.4.13</version>
     				
                     <!-- 新加部分开始 -->
                     <executions>
                         <execution>
                             <id>build-images</id>
                             <phase>package</phase>
                             <goals>
                                 <goal>build</goal>
                             </goals>
                         </execution>
                     </executions>
                      <!-- 新加部分结束 -->
     
                     <configuration>
                         <!-- leegoo0820/spring-cloud-eureka-server01-service(name):0.0.1(tag)-->
                         <imageName>leegoo0820/spring-cloud-eureka-server01-service:0.0.1</imageName>
                         <!-- 基础镜像 -->
                         <baseImage>java</baseImage>
                         <entryPoint>["java","-jar","/${project.build.finalName}.jar"]</entryPoint>
                         <resources>
                             <resource>
                                 <targetPath>/</targetPath>
                                 <!-- 目录地址,也就是target生成的地址 -->
                                 <directory>${project.build.directory}</directory>
                                 <!--需要复制的jar包 -->
                                 <include>${project.build.finalName}.jar</include>
                             </resource>
                         </resources>
                     </configuration>
                 </plugin>
     
             </plugins>
         </build>
     ```
     
     
     
3. 安装`docker-compose`(linux)

   - 下载`docker-compose` (前提必须有docker)

   ```shell
   sudo curl -L "https://github.com/docker/compose/releases/download/1.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
   
   ```
   - 授权`docker-compose`

     ```shell
     sudo chmod +x /usr/local/bin/docker-compose
     
     ```

   - 建立软链接(类似于在path 增加了`docker-compose`)

     ```shell
     sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
     ```

   - 检查是否安装完成

     ```shell
     [root@localhost my_files]# docker-compose --version
     docker-compose version 1.24.0, build 0aa59064
     ```

   - 增加`docker-compose`自动补全功能 (Place the completion script in `/etc/bash_completion.d/`)

     ```shell
      sudo curl -L https://raw.githubusercontent.com/docker/compose/1.24.0/contrib/completion/bash/docker-compose -o /etc/bash_completion.d/docker-compose
     
     ```
     

4. 使用docker-compose 进行服务编排

   - 首先在项目下创建`docker-compose.yml`文件, 比如我的项目名是 `spring-cloud-eureka-server`  ,那么就是`spring-cloud-eureka-server/docker-compose.yml`

   - 编辑`docker-compose.yml`内容

     ```yaml
     version: "2"
     
     services:
       eurela:
         image: spring-cloud-eureka-server_eurela
         volumes:  #挂载目录
           - "/usr/tmp/data/eureka"
         container_name: spring-cloud-eureka-server_eurela
         build: .  #找Dockerfile文件位置,  .代表当前文件下的Dockerfile文件
         ports:  #开放端口
           - "8767:8761"
     ```

   - 将项目传入到linux 服务器,随后到达项目的目录下 ,执行`docker-compose `命令

     ```shell
     docker-compose up  -d
     #-d 后台运行
     #如果要删除刚运行的容器
     # docker-compose stop   项目名 没有的话默认停止所有
     # docker-compose rm    项目名 没有的话默认删除所有
     ```


5.  使用`docker-compose` 同时部署多个docker 应用

   - 修改`spring-cloud-eureka-client01`中`pom.xml`配置,加入如下内容 [链接](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-client01/pom.xml):

   - 修改`spring-cloud-eureka-server01`中的 `pom.xml`,加入如下内容 [链接](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-server01/spring-cloud-eureka-server/pom.xml),

   - 修改`spring-cloud-eureka-server02`中的 `pom.xml`.加入如下内容 [链接](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-server02/spring-cloud-eureka-server/pom.xml),

     ```xml
     
         <build>
             <plugins>
     
                 <plugin>
                     <groupId>com.spotify</groupId>
                     <artifactId>docker-maven-plugin</artifactId>
                     <version>0.4.13</version>
     
                     <configuration>
                         <!-- leegoo0820/spring-cloud-eureka-server01-service(name):0.0.1(tag)-->
                         <imageName>leegoo0820/${project.artifactId}:${project.version}</imageName>
                         <forceTags>true</forceTags>
                         <!-- 基础镜像 -->
                         <baseImage>java</baseImage>
                         <entryPoint>["java","-jar","/${project.build.finalName}.jar"]</entryPoint>
                         <resources>
                             <resource>
                                 <targetPath>/</targetPath>
                                 <!-- 目录地址,也就是target生成的地址 -->
                                 <directory>${project.build.directory}</directory>
                                 <!--需要复制的jar包 -->
                                 <include>${project.build.finalName}.jar</include>
                             </resource>
                         </resources>
                     </configuration>
                 </plugin>
     
             </plugins>
         </build>
     
     ```

   - 修改 `spring-cloud-eureka-client01` 中 `application.yml`配置

   - 修改 `spring-cloud-eureka-server01中 `application.yml`配置

   - 修改 `spring-cloud-eureka-server02` 中 `application.yml`配置

     ```yaml
     eureka:
       client:
         service-url:
         	#此处新加配置 ,因为docker 中使用localhost是不能找到eureka服务器的,宿主机都是单独的ip段 
         	#client 需要绑定多个eureka
         	#但是servier01 和但是servier02 只需要对方的 服务编排 "名字"即可,下方会增加服务编排 spring-cloud-eureka-server01 和 spring-cloud-eureka-server02配置
           defaultZone: http://spring-cloud-eureka-server01:8761/eureka/,http://spring-cloud-eureka-server02:8761/eureka/
       instance:
         prefer-ip-address: true  #ip注册
     ```

   - 增加`docker-compose.yml` ,内容如下,[直达链接](spring-cloud/spring-cloud-eureka/docker-compose.yml)

     ```yaml
     version: "2"
     
     services:
       #此处就是服务编排的名称
       spring-cloud-eureka-server01:
         #image: 此处指定生成的image ,如果没有的话可能会去docker.io下载
         image: leegoo0820/spring-cloud-eureka-server01-service:0.0.1-SNAPSHOT
         volumes:  #挂载目录
           - "/usr/tmp/data/spring-cloud-eureka-server01"
         container_name: spring-cloud-eureka-server_eureka01
         build: .
         ports:  #开放端口
           - "8761:8761"
         #links:
         #  - spring-cloud-eureka-server02:discovery
         #environment:  设置环境参数
         #  - spring.profile.active=dev
     
       spring-cloud-eureka-server02:
         #image: java 指定镜像名或者id,没有就会下载
         image: leegoo0820/spring-cloud-eureka-server02-service:0.0.1-SNAPSHOT
         volumes:  #挂载目录
           - "/usr/tmp/data/spring-cloud-eureka-server02"
         container_name: spring-cloud-eureka-server_eureka02
         build: .
         ports:  #开放端口
           - "8762:8762"
     
       spring-cloud-eureka-client01:
         image: leegoo0820/spring-cloud-eureka-client01:0.0.1-SNAPSHOT
         volumes:  #挂载目录
           - "/usr/tmp/data/spring-cloud-eureka-client01"
         container_name: spring-cloud-eureka-client01
         build: .
         ports:  #开放端口
           - "8846:8846"
     
     
     ```

   - 访问`http://192.168.109.133:8846/say`  返回servier结果



------



## **Dubbo 部分**

1. 调用图 TODO

2. 错误分析

   - 从apach-dubbo 官网上面 抄袭了一份 简单的`consumer `和`provider`,结果发现`provider `"正常启动"(其实使用zkCli 连接上去看信息,发现只是注册了一个接口,但是好像没有任何内容),当时没太注意,紧接着又启动了`consumer `,结果发现`consumer`一直卡着不动 

     ```java
     //这是consumer的代码,理论上会执行完,但是执行到打印之前就会一直卡住
     ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-demo-consumer.xml");
     context.refresh();
     // Executing remote methods
     DemoService bean = context.getBean(DemoService.class);
     System.out.println("bean:"+bean);
     String hello = bean.sayHello("world");
     // Display the call result
     System.err.println("========"+hello);
     ```
     
     后来想了一下是不是zk的问题,发现zkServer版本是`3.5.5` 但是客户端(`consumer 和 provider 中的 pom.xml`)用的好像是`3.3.3` ,于是把`pom.xml`都改成了`3.5.5`后,发现`consumer`正常的访问了`provider`并且返回了结果,使用zkCli 去查看也有`provider 和consumer`的信息

   

3. 源码分析(基于版本`2.5.x`)

   - 报错`No such any registry to reference` 源码地址 `com.alibaba.dubbo.config.ReferenceConfig.createProxy`

```java
 @SuppressWarnings({"unchecked", "rawtypes", "deprecation"})
    private T createProxy(Map<String, String> map) {
        URL tmpUrl = new URL("temp", "localhost", 0, map);
        final boolean isJvmRefer;
        if (isInjvm() == null) {
            if (url != null && url.length() > 0) { // if a url is specified, don't do local reference   也就是说在配置文件里面配置了url的话,那么就可能是 点对点或者是从注册中心取provider url (由下方推断出)
                isJvmRefer = false;
            } else if (InjvmProtocol.getInjvmProtocol().isInjvmRefer(tmpUrl)) {
                // by default, reference local service if there is
                //默认引用本地服务
                isJvmRefer = true;
            } else {
                isJvmRefer = false;
            }
        } else {
            isJvmRefer = isInjvm().booleanValue();
        }

        if (isJvmRefer) { //本地服务
            URL url = new URL(Constants.LOCAL_PROTOCOL, NetUtils.LOCALHOST, 0, interfaceClass.getName()).addParameters(map);
            invoker = refprotocol.refer(interfaceClass, url);
            if (logger.isInfoEnabled()) {
                logger.info("Using injvm service " + interfaceClass.getName());
            }
        } else {
            if (url != null && url.length() > 0) { // user specified URL, could be peer-to-peer address, or register center's address.
                //此处使用的就是点对点或者是从注册中心取provider url 
                //也就是可能是  <dubbo:registry id="qdRegistry" address="10.20.141.150:9090" subscribe="false" />

                String[] us = Constants.SEMICOLON_SPLIT_PATTERN.split(url);
                //处理url
                if (us != null && us.length > 0) {
                    for (String u : us) {
                        URL url = URL.valueOf(u);
                        if (url.getPath() == null || url.getPath().length() == 0) {
                            url = url.setPath(interfaceName);
                        }
                        if (Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
                            urls.add(url.addParameterAndEncoded(Constants.REFER_KEY, StringUtils.toQueryString(map)));
                        } else {
                            //合并url 和请求参数
                            urls.add(ClusterUtils.mergeUrl(url, map));
                        }
                    }
                }
            } else { // assemble URL from register center's configuration
                //从注册中心取配置
                //读取的是 dubbo.registry.address 所指向的地址  false代表是不是提供者
                List<URL> us = loadRegistries(false);
                if (us != null && us.size() > 0) {
                    for (URL u : us) {
                        URL monitorUrl = loadMonitor(u);
                        if (monitorUrl != null) {
                            map.put(Constants.MONITOR_KEY, URL.encode(monitorUrl.toFullString()));
                        }
                        urls.add(u.addParameterAndEncoded(Constants.REFER_KEY, StringUtils.toQueryString(map)));
                    }
                }
                //如果从注册中心取的服务端列表为空,那么就会报错
                if (urls == null || urls.size() == 0) {
                    throw new IllegalStateException("No such any registry to reference " + interfaceName + " on the consumer " + NetUtils.getLocalHost() + " use dubbo version " + Version.getVersion() + ", please config <dubbo:registry address=\"...\" /> to your spring config.");
                }
            }

            if (urls.size() == 1) {
                //点对点
                invoker = refprotocol.refer(interfaceClass, urls.get(0));
            } else {
                List<Invoker<?>> invokers = new ArrayList<Invoker<?>>();
                URL registryURL = null;
                for (URL url : urls) {
                    invokers.add(refprotocol.refer(interfaceClass, url));
                    //Constants.REGISTRY_PROTOCOL  = "registry"
                    if (Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
                        registryURL = url; // use last registry url
                        //使用最后一个注册url  
                    }
                }
                if (registryURL != null) { // registry url is available
                    // use AvailableCluster only when register's cluster is available
                    //使用可用的集群配置
                    URL u = registryURL.addParameter(Constants.CLUSTER_KEY, AvailableCluster.NAME);
                    invoker = cluster.join(new StaticDirectory(u, invokers));
                } else { // not a registry url
                    invoker = cluster.join(new StaticDirectory(invokers));
                }
            }
        }

        Boolean c = check;
        if (c == null && consumer != null) {
            c = consumer.isCheck();
        }
        if (c == null) {
            c = true; // default true
        }
        if (c && !invoker.isAvailable()) {
            throw new IllegalStateException("Failed to check the status of the service " + interfaceName + ". No provider available for the service " + (group == null ? "" : group + "/") + interfaceName + (version == null ? "" : ":" + version) + " from the url " + invoker.getUrl() + " to the consumer " + NetUtils.getLocalHost() + " use dubbo version " + Version.getVersion());
        }
        if (logger.isInfoEnabled()) {
            logger.info("Refer dubbo service " + interfaceClass.getName() + " from url " + invoker.getUrl());
        }
        // create service proxy
        //到此处才会真正的进行调用
        return (T) proxyFactory.getProxy(invoker);
    }

```

- ​	dubbo 获取zk注册信息

  







