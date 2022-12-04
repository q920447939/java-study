/**
 * @Project:
 * @Author: leegoo
 * @Date: 2022年11月29日
 */
package cn.withme.socket;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GreetingClient
{
    public static void main(String [] args)
    {
        String serverName = "127.0.0.1";
        int port = 8888;
        StringBuffer process;
        int character;
        try
        {
            Socket client = new Socket(serverName, port);
            OutputStream outToServer = client.getOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(outToServer);
            out.write("test\n" .getBytes(StandardCharsets.UTF_8));
            out.flush();
            InputStream inFromServer = client.getInputStream();
            BufferedInputStream is = new BufferedInputStream(inFromServer);
            InputStreamReader isr = new InputStreamReader(is);
            process = new StringBuffer();
            while((character = isr.read()) != 10) {
                process.append((char)character);
            }
            System.out.println("process==="+process);
            client.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
