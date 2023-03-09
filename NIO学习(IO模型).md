## NIO学习(IO模型)

#### Java IO 模型总共分为下面几类。

1. **BIO**（Block IO）
   
   - 单线程阻塞IO，每次服务端接收到一个客户端的请求后，需要处理完成才能接收下一个客户端请求。
     
     ```java
     Socket socket = null;
     while (true) {
         //主程序阻塞在accept操作上
         socket = serverSocket.accept();
         doSomeThing();
     }
     ```
   
    - 多线程阻塞IO，每次服务端接收到一个客户端的请求后，通过线程池的方式去处理客户端请求。
   
      ```java
      Socket socket = null;
      while (true) {
          //主程序阻塞在accept操作上
          socket = serverSocket.accept();
          new Thread((Runnable)->{
              doSomeThing();
          });
      }
      ```

2. **NIO**（NEW IO）

   NIO 弥补了同步阻塞I/O的不足，它提供了高速、面向块的I/O，我们对一些概念介绍一下：

   **Buffer**: Buffer用于和NIO通道进行交互。数据从通道读入缓冲区，从缓冲区写入到通道中,它的主要作用就是和Channel进行交互。

   **Channel**: Channel是一个通道，可以通过它读取和写入数据，通道是双向的，通道可以用于读、写或者同时读写。

   **Selector**: Selector会不断的轮询注册在它上面的Channe,如果Channel上面有新的连接读写事件的时候就会被轮询出来，一个Selector可以注册多个Channel，**只需要一个线程负责Selector轮询，就可以支持成千上万的连接，可以说为高并发服务器的开发提供了很好的支撑**。
   
   简单来说：可以把很多`channel`注册到`select上`，`select`是一个单独的线程。每次`select`都会去轮训查看是否有新的事件，如果有新的事件，那么就可以告诉具体的`channel`
   
   详细可见：[NIO学习(reactor模型客户端与服务端)](./NIO学习(reactor模型客户端与服务端))
   
   **优点： 将`Reactor(AccpetHandler)`与实际的执行器进行分离。有利于解耦(`AcceptHandler`和`EchoHandler`同时实现了一个接口。可以通过接口编程)。**

   **缺点： 由于是单线程的`Reactor`。如果有一个连接被卡住了。那么后续的连接就不能继续执行了。**
