
## **<a id="t2">错误分析</a>**
1. 
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

## **<a id="t3">源码分析(基于版本`2.5.x`)</a>**
1. 

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

