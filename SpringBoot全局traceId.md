### 依赖

| 依赖       | 版本号                    |
| ---------- | ------------------------- |
| SpringBoot | 2.5.3                     |
| log4j2     | 适配SpringBoot(默认2.5.3) |
| 系统环境   | windows                   |

如果没有接入到Log4j2 。参考文章[SpringBoot适配异步Log4j](./SpringBoot适配异步Log4j.md)

### 原理解析

采用的方案是MDC

MDC（Mapped Diagnostic Context，映射调试上下文）是 log4j 、logback及log4j2 提供的一种方便在多线程条件下记录日志的功能。MDC 可以看成是一个与当前线程绑定的哈希表，可以往其中添加键值对。MDC 中包含的内容可以被同一线程中执行的代码所访问。当前线程的子线程会继承其父线程中的 MDC 的内容。当需要记录日志时，只需要从 MDC 中获取所需的信息即可。MDC 的内容则由程序在适当的时候保存进去。对于一个 Web 应用来说，通常是在请求被处理的最开始保存这些数据

#### API说明：

- clear() => 移除所有MDC
- get (String key) => 获取当前线程MDC中指定key的值
- getContext() => 获取当前线程MDC的MDC
- put(String key, Object o) => 往当前线程的MDC中存入指定的键值对
- remove(String key) => 删除当前线程MDC中指定的键值对

### 开始

我们知道`HTTP`请求是会经过`spring` [过滤器,拦截器](https://blog.csdn.net/heweimingming/article/details/79993591)

所以我们在过滤器中设置MDC

#### 1.`Filter`设置MDC值

```java
@Order(20)
@Component
public class GlobalFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        MDC.put("trace-id", httpRequest.getParameter("requestId"));// 这里我们是用前端生成的请求id放到MDC当中
        chain.doFilter(request, response);
    }
}

```

这样配置后,会产生一个问题,当把任务放到线程池当中,`MDC`会丢失,因为`MDC`是ThreadLocal的实现

#### 2.多线程设置MDC

我们需要实现`java.util.concurrent.ThreadPoolExecutor`，在执行前把`MDC`放到下一个线程的`ThreadLocal`当中

```java
public class WrapThreadPoolExecutor  extends ThreadPoolExecutor {

    public WrapThreadPoolExecutor(int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue,
                                  ThreadFactory threadFactory,
                                  RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable task) {
        super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()), result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }
        @Override
    protected void afterExecute(Runnable r, Throwable t) {
        //我们这里重写了afterExecute方法。
        //在线程处理完成后,会将MDC和ThreadContext清除,防止内存溢出。
        org.apache.log4j.MDC.clear();
        ThreadContext.clearAll();
        super.afterExecute(r, t);
    }
}

```

```java
public class ThreadMdcUtil {
    public static void setTraceIdIfAbsent() {
        // 如果有requestId,那么将requestId 放到下一个线程的MDC中
        if (StrUtil.isBlank(MDC.get(RequestConstant.MDC_TRA_ID))) {
            MDC.put(RequestConstant.MDC_TRA_ID, IdUtil.randomUUID());
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                // 在运行完成后,清理MDC,防止内存溢出
                MDC.clear();
            }
        };
    }


    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
```

引用

https://blog.csdn.net/heweimingming/article/details/79993591

https://www.codenong.com/cs106578420/

https://www.docs4dev.com/docs/zh/log4j2/2.x/all/manual-layouts.html#PatternLayout

