## **<a id="t1">锁住同一个对象就能保证多线程问题吗?</a>**

首先看程序,在主程序中 打印了 "i"的值,那么会是多少呢?

```java
public class BadSynOnInteger implements  Runnable{
    static BadSynOnInteger badSynOnInteger = new BadSynOnInteger();
    static  Integer i  = 0 ;

    @Override
    public void run() {
        for (int l = 0; l < 1000000; l++) {
            synchronized (i) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BadSynOnInteger badSynOnInteger = new BadSynOnInteger();
        Thread t1 = new Thread(badSynOnInteger);
        Thread t2 = new Thread(badSynOnInteger);
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println(i);
    }
}


```



答案是不会达到`2000000`,无论运行多少次结果也是一样

为什么会这样呢?我明明锁住了类变量啊,就算`i++` 不是原子操作结果应该也是`2000000`才对啊



我们可以看看字节码,看看到底字节码对我们的代码进行了怎样的处理

1. 来到当前类(`BadSynOnInteger`)的的目录下

2. 使用`javac -encoding UTF-8 BadSynOnInteger.java`手动编译成`.class`文件

3. 使用`javap -c -v BadSynOnInteger.class >> m.txt` 查看汇编指令(内容比较多,所以输入到当前目录的m.txt文本文档内)

4. 截取部分内容如下:

   ```
     public void run();
       descriptor: ()V
       flags: ACC_PUBLIC
       Code:
         stack=2, locals=6, args_size=1
            0: iconst_0
            1: istore_1
            2: iload_1
            3: ldc           #2                  // int 1000000
            5: if_icmpge     55
            8: getstatic     #3                  // Field i:Ljava/lang/Integer;
           11: dup
           12: astore_2
           13: monitorenter  //获取锁
           14: getstatic     #3                  // Field i:Ljava/lang/Integer;
           17: astore_3
           18: getstatic     #3                  // Field i:Ljava/lang/Integer;
           21: invokevirtual #4                  // Method java/lang/Integer.intValue:()I
           24: iconst_1
           25: iadd
           26: invokestatic  #5                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
           29: dup
           30: putstatic     #3                  // Field i:Ljava/lang/Integer;
           33: astore        4
           35: aload_3
           36: pop
           37: aload_2
           38: monitorexit  //释放锁
   ```

   

   ​	我们发现`i++`操作在汇编中变成了这样

   ```
           21: invokevirtual #4                  // Method java/lang/Integer.intValue:()I
           24: iconst_1
           25: iadd
           26: invokestatic  #5                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
           29: dup
           30: putstatic     #3                  // Field i:Ljava/lang/Integer;
   ```

   

   

   21行对应java代码

   ```java
       /**
        * Returns the value of this {@code Integer} as an
        * {@code int}.
        */
       public int intValue() {
           return value;
       }
   ```

   

   26行java代码

   ```java
       public static Integer valueOf(int i) {
           if (i >= IntegerCache.low && i <= IntegerCache.high)
               return IntegerCache.cache[i + (-IntegerCache.low)];
           return new Integer(i);
       }
   ```

   那么我们现在大概就已经知道结果了,首先调用`Integer.intValue`方法,将值转为`Int`类型,

   然后做`iadd`操作

   随后调用`Integer.valueOf`将int类型的值转为`Integer`

   注意`valueOf()`返回的内容,是`new`了一个新的对象

   可能你会问为什么需要`new`一个新的对象,用原来那一个不就好了吗

   那么可以去看一下`Integer`类是不是有`final`修饰符

   同样的,类似于`String str = “abc"; str+="cde"` 这样操作也可能并非像想象的结果一样

   
   
   如果将`synchronized`(i)改为`synchronized(badSynOnInteger)`结果又是怎么样呢?
   
   如果将`t1.join(); t2.join();`注释掉,那么结果又是怎么样呢?
   
   [源码地址](java-bases/src/main/java/cn/withme/thread/BadSynOnInteger.java)