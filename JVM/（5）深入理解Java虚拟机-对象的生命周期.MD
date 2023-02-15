# 对象的生命周期


Java对象的生命周期包括：加载（Loading）、验证（Verification）、准备（Preparation）、解析（Resolution）、初始化
（Initialization）、使用（Using）和卸载（Unloading）七个阶段，其中验证、准备、解析三个部分统称
为连接（Linking）。

![image-20230208163854162.png](https://s2.loli.net/2023/02/08/tkcVbI2Td9HvhnP.png)
#### 加载

问：什么时候会产生加载？

答：当遇到`new,invokestatic,getstatic,putstatic`指令会产生加载；使用反射会产生加载（类没有没初始化过）；初始化子类之前子类的构造函数会初始化父类。（还有一些其他的特殊场景未列出来）。

只需要把`字节流`送给`ClassLoad(类加载器)`。于是乎可以用多种方式加载`class`，常见的比如用`URL,ZIP(JAR,WAR)`的方式加载类。



#### 验证

验证加载部分送进来的`字节流`是否符合虚拟机的规范。



#### 准备

类中的静态变量初始化0值。

```java
public static final int value = 123;
```

比如这一句，在准备阶段`value = 0 `

### 解析

将类中的符号引用转为直接引用。[Java虚拟机 - 符号引用和直接引用理解 )](https://www.cnblogs.com/qlky/p/7643524.html)

在我们编写代码阶段，没一个类中引用其他的类对象是一串`“字符串”`，

```java
public class Student{
    
}

public class Teacher{
    private List<Student> students ;
}
```

比如一个`Teacher`可以管理多个`Student`，在`Teacher`类中有一个属性`students`，在我们加载到内存之前，这个`students`只是一个"字符串"引用对象。当`Teacher`加载到内存中去的时候。那么`students`所对应的`Student类`集合肯定会对应一个内存地址。

这也就是符号引用转到直接引用的过程。

当然还包括方法解析



#### 初始化

在`准备`阶段。`value`值还是`0`，到了初始化阶段，`value`值会被赋值用户设置的值。



