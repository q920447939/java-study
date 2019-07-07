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

   - 使用maven命令进行打包(需要提前配置maven 环境,和java环境才能进行打包):

     ```shell
     mvn clean package docker:build
     ```

   - 然后使用`docker images` 会看到 `spring-cloud-eureka-server01-service` 镜像

   - 随后启动 

     ```shell
     [root@localhost spring-cloud-eureka-server]# docker run -p 8761:8761 --name my_eureka leegoo0820/spring-cloud-eureka-server01-service:0.0.1
     ```

     