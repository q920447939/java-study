# 深入理解Java虚拟机-内存溢出

这部分需要了解，虽然有GC帮助我们回收没有使用的对象进行释放内存操作。但是一些不恰当的代码或者配置将会导致内存溢出的情况发生。

### 堆溢出

手动控制堆的最小和最大容量。在`while`语句中频繁创建对象让`GC`无法回收导致内存溢出。

```java
/**
 * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * @author zzm
 */
public class HeapOOM {
    static class OOMObject {
    }
    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
```



### 栈溢出

栈溢出的场景一般是`递归`算法时才会产生。

这里手动调整栈的大小（这样可以更快的出现`StackOverflowError`）

```java
/**
 * VM Args：-Xss128k
 * @author zzm
 */
public class JavaVMStackSOF {
    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) throws Throwable {
        JavaVMStackSOF oom = new JavaVMStackSOF();
        try {
            oom.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length:" + oom.stackLength);
            throw e;
        }
    }
}
```





### 方法区和运行时常量池

方法区保存的是一些类的信息数据。此场景触发的情况一般是类加载（新产生的类，并非对象）的很频繁，导致GC无法回收产生异常

注意在JDK8，由于没有`永久带`的概念，只有`元数据区`的概念。所以增加`VM`参数 =》 `-XX:MaxMetaspaceSize=10M`,这样能够更加准确的测试到`Metaspace`异常。

```java
/**
 *
 * VM Args：-XX:PermSize=10M -XX:MaxPermSize=10M
 * JDK7 运行一段时候后出现
 *  Caused by: java.lang.OutOfMemoryError: PermGen space（未测试）
 *JDK8 运行一段时候后出现
 *  Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
 *
 *  VM Args：-XX:MaxMetaspaceSize=10M
 *  JDK8 运行一段时候后出现
 *  Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
 * @author zzm
 */
public class JavaMethodAreaOOM {
    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            enhancer.create();
        }
    }
    static class OOMObject {
    }
}

```



#### 运行时常量池导致的`==`不同

```java
public class RuntimeConstantPoolOOM2 {
    //JDK8 场景
    public static void main(String[] args) {
        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1); // false
        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2); // true
    }
}

```

为什么在`JDK8`下面，会出现一个`false`,一个为`true`的情况？

因为使用`intern()`方法会把字符串的值放到`运行时常量池中`并返回`运行时常量池中`的对象引用（`运行时常量池中`没有该字符串）。

所以，`计算机`软件 在常量池中没有出现，那么`str1.intern()`返回的是一个新的地址，由于`==`比较的是地址， 即与`str1`的地址相比结果为`false`

`java`这个字符串在调用`main`方法之前就会放到`运行时常量池`中（见`sun.misc.Version`类），所以已经在`运行时常量池`存在了，当`str2 = new StringBuilder("ja").append("va").toString();`,实际上是取了`运行时常量池`中 `java`字符串的引用地址，而`str2.intern()`也是取了`运行时常量池`中 `java`字符串的引用地址，所以结果为`true`





### 本机直接内存溢出

​		直接内存（Direct Memory）的容量大小可通过-XX：MaxDirectMemorySize参数来指定，如果不

去指定，则默认与Java堆最大值（由-Xmx指定）一致。暂未测试