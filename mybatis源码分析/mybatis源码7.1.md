#获取session，以及拦截器（Interceptor）
上一章我们知道`mybatis`最终会创建出一个代理对象（`org.apache.ibatis.binding.MapperProxyFactory.newInstance(org.apache.ibatis.binding.MapperProxy<T>)`）

我们现在通过一个简单的DAO查询。跟踪`mybatis`的运行流程

DAO 接口 (`cn.withmes.springboot.mybatis.interceptor.mapper.UserMapper`)
```java
    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
        return newInstance(mapperProxy);
      }

    protected T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
    }
  
```


最终会调用 `org.apache.ibatis.binding.MapperProxy.invoke`
```java
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      /*
              这里没有细看。应该是判断是否是Object方法。
              如果是object方法就直接返回了
       */
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, args);
      } else if (isDefaultMethod(method)) {
        return invokeDefaultMethod(proxy, method, args);
      }
      //增加一个Mapper缓存。 不重要
    final MapperMethod mapperMethod = cachedMapperMethod(method);
    //这里把参数sqlSession 和 参数args传进去   调用  代码标记2 代码
    return mapperMethod.execute(sqlSession, args);
  }

  
```


```java
  //代码标记2
  public Object execute(SqlSession sqlSession, Object[] args) {
    Object result;
    //不相关的代码已经移除。
    // 解析参数    
    Object param = method.convertArgsToSqlCommandParam(args);
     /**
     注意这里sqlSession是一直通过入参传进来的。
     那么sqlSession是从哪里来的呢？
     其实是org.apache.ibatis.binding.MapperProxyFactory#newInstance(org.apache.ibatis.session.SqlSession)。可看最上一个代码块中的代码。
     */
    result = sqlSession.selectOne(command.getName(), param);
    if (method.returnsOptional()
    && (result == null || !method.getReturnType().equals(result.getClass()))) {
    result = Optional.ofNullable(result);
    }
    if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
      throw new BindingException("Mapper method '" + command.getName()
          + " attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
    }
    return result;
  }

  @Override
  public <T> T selectOne(String statement, Object parameter) {
    //这里的sqlSessionProxy是一个成员变量。
    //那么它又是从哪来的呢？
    // 答案是创建SqlSessionTemplate构造方法的时候创建出来的（ org.mybatis.spring.SqlSessionTemplate#SqlSessionTemplate(org.apache.ibatis.session.SqlSessionFactory, org.apache.ibatis.session.ExecutorType, org.springframework.dao.support.PersistenceExceptionTranslator) ）
    //触发点还是MybatisAutoConfiguration（org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration#sqlSessionTemplate）  具体可看mybatis源码4.md
    return sqlSessionProxy.selectOne(statement, parameter);
  }

public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
      PersistenceExceptionTranslator exceptionTranslator) {
    this.sqlSessionFactory = sqlSessionFactory;
    this.executorType = executorType;
    this.exceptionTranslator = exceptionTranslator;
    //这里实例化 sqlSessionProxy。实际上是创建了一个代理对象。
    // 当调用 this.sqlSessionProxy.xx方法时。 首先肯定会调用 SqlSessionInterceptor#invoke方法。 如果这里还不清楚的话 建议同学先复习一下JDK动态代理
    this.sqlSessionProxy = (SqlSession) newProxyInstance(
        SqlSessionFactory.class.getClassLoader(),
        new Class[] { SqlSession.class },
        new SqlSessionInterceptor());
  }

```

```java

    /**
   * Proxy needed to route MyBatis method calls to the proper SqlSession got
   * from Spring's Transaction Manager
   * It also unwraps exceptions thrown by {@code Method#invoke(Object, Object...)} to
   * pass a {@code PersistenceException} to the {@code PersistenceExceptionTranslator}.
    这一块意思应该是 代理需要经过mybatis的方法。把SqlSession交给Spring事务管理。同时也不会包装异常 
   */
  private class SqlSessionInterceptor implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      SqlSession sqlSession = getSqlSession(
          SqlSessionTemplate.this.sqlSessionFactory,
          SqlSessionTemplate.this.executorType,
          SqlSessionTemplate.this.exceptionTranslator);
      //下面的mybatis源码7.2 篇章继续
        Object result = method.invoke(sqlSession, args);
        if (!isSqlSessionTransactional(sqlSession, SqlSessionTemplate.this.sqlSessionFactory)) {
          // force commit even on non-dirty sessions because some databases require
          // a commit/rollback before calling close()
          sqlSession.commit(true);
        }
        return result;
    }

  
  /**
   * Gets an SqlSession from Spring Transaction Manager or creates a new one if needed.
   * Tries to get a SqlSession out of current transaction. If there is not any, it creates a new one.
   * Then, it synchronizes the SqlSession with the transaction if Spring TX is active and
   * <code>SpringManagedTransactionFactory</code> is configured as a transaction manager.
   从 Spring Transaction Manager 获取 SqlSession 或在需要时创建一个新的。
   尝试从当前事务中获取 SqlSession。如果没有，它会创建一个新的。
   然后，如果 Spring TX 处于活动状态并且 SpringManagedTransactionFactory 被配置为事务管理器，它会将 SqlSession 与事务同步。
   */
  public static SqlSession getSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator) {
    /*
    从事务管理中获取一个  SqlSessionHolder
    事务不是本章研究的问题。暂不研究代码
    SqlSessionHolder 可以简单像spirngbeandifinitionholder理解。 只是beandifinition的一个包装
    */
    SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
	//获取一个 session。如果获取到了就直接返回了。 和注释的意思差不多。
    SqlSession session = sessionHolder(executorType, holder);
    if (session != null) {
      return session;
    }
    //没有获取到就重新生成一个session 。注意这里的sessionFactory 还是DefaultSqlSessionFactory（org.apache.ibatis.session.defaults.DefaultSqlSessionFactory）
    session = sessionFactory.openSession(executorType);
    return session;
  }


  @Override //org.apache.ibatis.session.defaults.DefaultSqlSessionFactory#openSession(org.apache.ibatis.session.ExecutorType)
  public SqlSession openSession(ExecutorType execType) {
    return openSessionFromDataSource(execType, null, false);
  }

  private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
    Transaction tx = null;
    try {
      //获取环境变量  
      final Environment environment = configuration.getEnvironment();
      //获得事务工厂
      final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
      //创建一个新的事务
      tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
      //获取执行器  
      final Executor executor = configuration.newExecutor(tx, execType);
      //executor = CachingExecutor
      return new DefaultSqlSession(configuration, executor, autoCommit);
    } catch (Exception e) {
      closeTransaction(tx); // may have fetched a connection so lets call close()
      throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
    } finally {
      ErrorContext.instance().reset();
    }
  }

  public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
      //先获取了一个SimpleExecutor
      Executor executor = new SimpleExecutor(this, transaction);
      //然后把SimpleExecutor放到CachingExecutor
      executor = new CachingExecutor(executor);
      //调用拦截器链
      executor = (Executor) interceptorChain.pluginAll(executor);
      // executor = CachingExecutor
     return executor;
  }

  public Object pluginAll(Object target) {
    //注意看下方我们自己实现的拦截器（ExecutorInterceptor）
    for (Interceptor interceptor : interceptors) {
       //所以这里会调用 plugin方法  。target其实是 CachingExecutor
      target = interceptor.plugin(target);
    }
    //由于 代码3没有匹配上target。所以这个target还是CachingExecutor
    return target;
  }


```


为了验证`mybatis`的拦截器。我们从网上copy一份`mybaits`拦截器代码
```java

//@Component不能少。少了不会被spring管理
@Component
//@Intercepts不能少。后面的代码好像会判断是否有这个注解
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class ExecutorInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        //上面的方法 interceptor.plugin(target); 会调用到这里 。 target其实是 CachingExecutor 
        // 调用 代码3
        return Plugin.wrap(target, this);
    }
    
    // 代码3
    public static Object wrap(Object target, Interceptor interceptor) {
        //这里存在一个判断。如果interfaces.length 那么就会返回一个代理对象。
        // Plugin#invoke
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        Class<?> type = target.getClass();
        Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
        //第一次返回是0
        if (interfaces.length > 0) {
          return Proxy.newProxyInstance(
              type.getClassLoader(),
              interfaces,
              new Plugin(target, interceptor, signatureMap));
        }
        //否则还是返回CachingExecutor
        return target;
   }

    
  private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
      //这里就会判断类上是否有@Intercepts注解
    Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
    if (interceptsAnnotation == null) {
      throw new PluginException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());
    }
    Signature[] sigs = interceptsAnnotation.value();
    Map<Class<?>, Set<Method>> signatureMap = new HashMap<>();
    for (Signature sig : sigs) {
      //sig.type() = StatementHandler
      Set<Method> methods = signatureMap.computeIfAbsent(sig.type(), k -> new HashSet<>());
      try {
        //sig.method() = prepare  
        //sig.args() Connection.class, Integer.class  
        //其实应该就是找到 StatementHandler#prepare(Connection.class, Integer.class  )
        Method method = sig.type().getMethod(sig.method(), sig.args());
        methods.add(method);
      } catch (NoSuchMethodException e) {
        throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
      }
    }
    return signatureMap;
  }
    
  private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
    Set<Class<?>> interfaces = new HashSet<>();
    while (type != null) {
      //  type = CachingExecutor
       //signatureMap 有一个元素  StatementHandler
      /*
      第一次获取 CachingExecutor的所有接口  ,CachingExecutor只实现了org.apache.ibatis.executor.Executor
      if (signatureMap.containsKey(c)) 不满足
      CachingExecutor = 它的超类。 CachingExecutor的超类是Object.class
      第二次循环。 Object.class 没有接口。所有就会退出while循环
      */  
      for (Class<?> c : type.getInterfaces()) {
        if (signatureMap.containsKey(c)) {
          interfaces.add(c);
        }
      }
      type = type.getSuperclass();
    }
    //这里返回是0
    return interfaces.toArray(new Class<?>[interfaces.size()]);
  }    
}

```

**总结**

通过`MapperProxyFactory`创建代理对象 -> 调用代理对象(`MapperProxy`)`invoke`方法
-> 条件分支查询selectOne->调用`sqlSessionProxy `创建的代码类(`SqlSessionInterceptor`)`invoke`方法
->获取`sqlSession`(`CachingExecutor`)
