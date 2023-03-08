/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月03日
 */
package cn.withmes.nioFile;

/**
 * ClassName: NIOReceviceFileServce
 * @Description:
 * @author leegoo
 * @date 2023年03月03日
 */
public class NIOReceviceFileServce {
    public static void main(String[] args) {
        //创建选择器
        //创建管道，非阻塞，绑定IP 端口 ，注册到选择器上  accept
        //获取选择器上面的selectKey
        //循环selectKey 获取感兴趣的key

        //如果是可接收状态
        //转成socketChannel ,
        //重新注册为可读到选择器上。 同时设置TCP-NOdelay
        //创建一个Session数据结构，用来保存channel里面的客户端IP 。

        //如果是可读模式
        //转成socketChannel
        //首先获取文件名称
        //获取文件大小
        //读文件内容
    }
}
