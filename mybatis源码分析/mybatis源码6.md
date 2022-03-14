上一章我们知道`MapperFactoryBean`初始化，会将`userMapper`接口的信息放到`map`集合中。

也就是说还没有创建代理对象

那么我们现在开始尝试从`spring`容器中获取`userMapper`对象。

//并调用其`findByName()`方法

我们只需在`org.mybatis.spring.mapper.MapperFactoryBean.getObject`打上断点（断点肯定会来这）。

```java
  public T getObject() throws Exception {
    //获取到sqlSession后，调用getMapper方法
    //this.mapperInterface 就是userMapper
    return getSqlSession().getMapper(this.mapperInterface);
  }

```

最终调用`org.apache.ibatis.binding.MapperRegistry.getMapper`
```java

  @SuppressWarnings("unchecked")
  public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
    if (mapperProxyFactory == null) {
      throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
    }
    try {
        //这里进行代理方法
      return mapperProxyFactory.newInstance(sqlSession);
    } catch (Exception e) {
      throw new BindingException("Error getting mapper instance. Cause: " + e, e);
    }
  }



  @SuppressWarnings("unchecked")
  protected T newInstance(MapperProxy<T> mapperProxy) {
          //使用JDK默认的代理 
          //为什么不用CGLIB ? ，因为mapper 只能是接口。
          //这里设置了代理。那么当调用的时候，实际上就会去调用 MapperProxy#invoke 方法
          return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
          }

  //第一步先调用这个方法，
  public T newInstance(SqlSession sqlSession) {
    //实例化一个MapperProxy
  final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
          return newInstance(mapperProxy);
          }

```
