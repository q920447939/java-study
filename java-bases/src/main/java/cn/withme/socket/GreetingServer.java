/**
 * @Project:
 * @Author: leegoo
 * @Date: 2022年11月29日
 */
package cn.withme.socket;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GreetingServer extends Thread
{
    private ServerSocket serverSocket;

    public GreetingServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run()
    {
        StringBuffer process;
        int character;
        while(true)
        {
            try
            {
                System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
                BufferedInputStream is = new BufferedInputStream(server.getInputStream());
                InputStreamReader isr = new InputStreamReader(is);
                process = new StringBuffer();
                while((character = isr.read()) != 10) {
                    process.append((char)character);
                }
                System.out.println(process);
                BufferedOutputStream out = new BufferedOutputStream(server.getOutputStream());
                out.write("收到消息,现在回复\n".getBytes(StandardCharsets.UTF_8));
                out.flush();

            }catch(SocketTimeoutException s)
            {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
    public static void main(String [] args)
    {
        int port = 8888;
        try
        {
            Thread t = new GreetingServer(port);
            t.run();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * stream转化为字节数组
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] streamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();// 创建输出流
        byte[] buffer = new byte[1024]; // 字节数组
        int len = 0;
        // read()方法是有参数的！
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer,0,len);
        }
        byte[] array = bos.toByteArray();//输出流转换为数组
        bos.close();
        return array;
    }
}
