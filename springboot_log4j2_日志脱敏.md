## SpringBoot log4j2 日志脱敏

### 起因

针对一些敏感数据，如果日志全部打印出来。那么及有可能会泄露敏感数据信息（比如银行卡，用户密码，地址等）

### 需求

1.针对打印日志 敏感数据 进行脱敏
2.为了有良好的回滚能力，需要增加开关 是否开启该功能
3.哪一些类，类中的哪些字段需要脱敏。需要做到自定义控制（比如在开发环境，我想验证某些功能，所以开关是开的，那么敏感数据就会脱敏，但是我们可能需要用到某些数据，这样就比较麻烦了）
### 解决方案

因为log4j有多种方案支持脱敏（比如说直接在xml里面进行关键字配置）

但是我们更多的是需要个性化的脱敏方案，比如说 `张三丰` ，我们可能想脱敏后的结果是 `张*丰` 或者是 `张**`

这里我采用的方案是**重写`Append`**



1. 前提（搭建了spring-boot 以及引入了log4j2）
2. 在append里面我们使用新增一个`Rewrite`标签
```java
    <Console name="CONSOLE" target="SYSTEM_OUT">
        <PatternLayout pattern="${PATTERN}" charset="${CHARSET}"/>
    </Console>

    <Rewrite name="obfuscateSensitiveData-CONSOLE">  //这个自定义的名字
        <AppenderRef ref="CONSOLE"/> //数据源是CONSOLE. 也就是CONSOLE的日志 会经过这里再进行处理一遍
        <MaskSensitiveDataPolicy/> // 这里是我们自定义的Append 名字（类名）。也就是需要知道具体要执行哪个类的逻辑
    </Rewrite>
```

3. 增加类注解，和属性注解
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogEncryptClz {
    boolean enable() default true;
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogEncryptFiled {
    DesensitizedUtil.DesensitizedType type();
}


```
4. 增加 `log4j2.enable.threadlocals = false`配置 。这里我直接在`resources`目录下新建`log4j2.component.properties`文件。

```properties
log4j2.enable.threadlocals=false
```


5. 重写类的逻辑(`MaskSensitiveDataPolicy`)

```java

@Plugin(name = "MaskSensitiveDataPolicy", category = Core.CATEGORY_NAME,
        elementType = "rewritePolicy", printObject = true)
public class MaskSensitiveDataPolicy implements RewritePolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaskSensitiveDataPolicy.class);


    private String[] sensitiveClasses;
    private volatile boolean encryptEnable; //这里最好通过开关控制一下功能是否开和关

    public static final Map<String, DesensitizedUtil.DesensitizedType> SENSITIVE_MAP = new HashMap<>();

    static {
        //DesensitizedUtil 这里引用的是hutools
        SENSITIVE_MAP.put("username", DesensitizedUtil.DesensitizedType.CHINESE_NAME);
        SENSITIVE_MAP.put("password", DesensitizedUtil.DesensitizedType.PASSWORD);
        SENSITIVE_MAP.put("cellphone", DesensitizedUtil.DesensitizedType.MOBILE_PHONE);
        SENSITIVE_MAP.put("email", DesensitizedUtil.DesensitizedType.EMAIL);
    }

    @PluginFactory
    public static MaskSensitiveDataPolicy createPolicy(
            @PluginElement("sensitive") final String[] sensitiveClasses) {
        return new MaskSensitiveDataPolicy(sensitiveClasses);
    }

    private MaskSensitiveDataPolicy(String[] sensitiveClasses) {
        super();
        encryptEnable = Boolean.parseBoolean(System.getProperty("log4j2.encrypt.enable"));
        this.sensitiveClasses = sensitiveClasses;
    }

    @Override
    public LogEvent rewrite(LogEvent event) {
        Message rewritten = rewriteIfSensitive(event.getMessage());
        if (rewritten != event.getMessage()) {
            return new Log4jLogEvent.Builder(event).setMessage(rewritten).build();
        }
        return event;
    }

    private Message rewriteIfSensitive(Message message) {
        // 确保已经通过设置系统属性`log4j2.enable.threadlocals` 为 `false`关闭了garbage-free logging
        // 否则可能传入ReusableObjectMessage, ReusableParameterizedMessage或
        // MutableLogEvent messages 导致不能重写。

        // Make sure to switch off garbage-free logging
        // by setting system property `log4j2.enable.threadlocals` to `false`.
        // Otherwise you may get ReusableObjectMessage, ReusableParameterizedMessage
        // or MutableLogEvent messages here which may not be rewritable...
        if (message instanceof ObjectMessage) {
            return rewriteObjectMessage((ObjectMessage) message);
        }
        if (message instanceof ParameterizedMessage) {
            return rewriteParameterizedMessage((ParameterizedMessage) message);
        }
        return message;
    }

    private Message rewriteObjectMessage(ObjectMessage message) {
        SensitiveStrategy sensitive = isSensitive(message.getParameter());
        if (encryptEnable && sensitive.coincidence()) {
            return new ObjectMessage(sensitive.strategy(message.getParameter()));
        }
        return message;
    }

    private Message rewriteParameterizedMessage(ParameterizedMessage message) {
        Object[] params = message.getParameters();
        boolean changed = rewriteSensitiveParameters(params);
        return changed && encryptEnable ? new ParameterizedMessage(message.getFormat(), params) : message;
    }

    private boolean rewriteSensitiveParameters(Object[] params) {
        boolean changed = false;
        for (int i = 0; i < params.length; i++) {
            SensitiveStrategy sensitive = isSensitive(params[i]);
            if (sensitive.coincidence()) {
                params[i] = sensitive.strategy(params[i]);
                changed = true;
            }
        }
        return changed;
    }

    private SensitiveStrategy isSensitive(Object parameter) {
        SensitiveStrategy defaultStrategy = new SensitiveStrategy() {
            @Override
            public boolean coincidence() {
                return false;
            }

            @Override
            public Object strategy(Object params) {
                return null;
            }
        };
        if (null == parameter)  return defaultStrategy;
        // TODo 加入缓存
        LogEncryptClz logEncryptClz = parameter.getClass().getAnnotation(LogEncryptClz.class);
        boolean objInfer = null != logEncryptClz && logEncryptClz.enable() && parameter.getClass().getPackage().getName().startsWith("com.你的包名");
        if (objInfer) {
            return new ObjectSensitiveStrategy(true);
        }
        if (parameter instanceof List) {
            return new ListSensitiveStrategy(true);
        }
        return defaultStrategy;
    }


    public interface SensitiveStrategy {
        boolean coincidence();

        Object strategy(Object params);
    }


    public static class ObjectSensitiveStrategy implements SensitiveStrategy {

        private final boolean coincidence;

        public ObjectSensitiveStrategy(boolean coincidence) {
            this.coincidence = coincidence;
        }

        @Override
        public boolean coincidence() {
            return this.coincidence;
        }

        @Override
        public Object strategy(Object parameter) {
            if (null == parameter) {
                return null;
            }
            return objectStrategy(parameter);
        }

        public static Object objectStrategy(Object parameter) {
            JSONObject json = (JSONObject) JSONObject.toJSON(parameter);
            Map<String, Field> fileNameMaps = Stream.of(ReflectUtil.getFields(parameter.getClass())).filter(field -> {
                return field.isAnnotationPresent(LogEncryptFiled.class);
            }).collect(Collectors.toMap(Field::getName, Function.identity(), (a, b) -> b));
            if (MapUtil.isEmpty(fileNameMaps)) {
                return parameter;
            }

            for (String key : json.keySet()) {
                Object v = json.get(key);
                if (null == v || !fileNameMaps.containsKey(key)) {
                    continue;
                }

                Field field = fileNameMaps.get(key);
                LogEncryptFiled annotation = field.getAnnotation(LogEncryptFiled.class);
                DesensitizedUtil.DesensitizedType desensitizedType = annotation.type();
                try {
                    json.put(key, DesensitizedUtil.desensitized(String.valueOf(json.get(key)), desensitizedType));
                } catch (Exception exception) {
                    LOGGER.error("【数据脱敏】处理脱敏数据异常 源数据={}", json, exception);
                }
            }
            return JSONObject.toJSON(json);
        }
    }

    public static class ListSensitiveStrategy implements SensitiveStrategy {

        private final boolean coincidence;

        public ListSensitiveStrategy(boolean coincidence) {
            this.coincidence = coincidence;
        }

        @Override
        public boolean coincidence() {
            return this.coincidence;
        }

        @Override
        public Object strategy(Object parameter) {
            if (null == parameter) {
                return null;
            }
            List list = (List) parameter;
            if (CollUtil.isEmpty(list)) return null;

            List result = new ArrayList();
            for (Object o : list) {
                LogEncryptClz annotation = o.getClass().getAnnotation(LogEncryptClz.class);
                if (null == annotation || !annotation.enable() || !o.getClass().getPackage().getName().startsWith("com.你的包名")) {
                    continue;
                }

                if (o instanceof List) {
                    result.add(strategy(o));
                    continue;
                }
                result.add(ObjectSensitiveStrategy.objectStrategy(o));
            }
            return result;
        }
    }
}

```

6. main 类
```java

@SpringBootApplication
public class SpringBootLogDesensitizationApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootLogDesensitizationApplication.class);


    public static void main(String[] args) {
        User mock = mockData();
        LOGGER.info("【启动类】打印脱敏对象 mock1={}", mock); // 此处不会打印，应该是与初始化有关
        loadProperties();
        SpringApplication.run(SpringBootLogDesensitizationApplication.class, args);
        LOGGER.info("【启动类】打印脱敏对象 mock2={}", mock);
    }

    private static User mockData() {
        return User.builder().id("1").name("王大锤").password("123456").build();
    }


    @SneakyThrows
    private static void loadProperties() {
        ClassPathResource resource = new ClassPathResource("system.properties");
        Properties properties = new Properties();
        properties.load(resource.getStream());
        properties.forEach((k, v) -> System.setProperty(String.valueOf(k), String.valueOf(v)));
        LOGGER.info("【启动类】获取到环境参数={}", properties);
    }

    @Data
    @Builder
    @LogEncryptClz
    public static class User {
        private String id;
        private String name;
        @LogEncryptFiled(type = DesensitizedUtil.DesensitizedType.PASSWORD)
        private String password;
    }

}


```

7. 最终效果
```java
[INFO ] [] [] 2021-12-22 14:19:51.920 [main]  cn.withmes.springboot.log.desensitization.SpringBootLogDesensitizationApplication[27] - 【启动类】打印脱敏对象 mock2={password=******, name=王大锤, id=1}

这里可以看到password字段已经脱敏了。

```



### 结论：

​	通过重写`Append` ，可以实现日志脱敏

​	具体代码可参考github：

​	参考：https://blog.csdn.net/rick_zyl/article/details/120016622

