/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月13日
 */
package cn.withme.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: SockerTest
 * @Description:
 * @author leegoo
 * @date 2019年06月13日
 */
public class SockerTest {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket();
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            Socket accept = socket.accept(); //必须接受到客户端的信号才能继续处理后面的程序
            //todo something
            //虽然使用线程池去处理后续的工作,但是前面的代码在没有接受到客户端的数据时还是会阻塞
            //而且这样利用线程池去处理任务的话,client一多,容易造成系统崩溃
            executorService.execute(()->{
                System.out.println("do work....");
            });
        }
    }
}
