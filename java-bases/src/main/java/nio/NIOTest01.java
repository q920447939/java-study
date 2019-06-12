/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月12日
 */
package nio;


import java.nio.ByteBuffer;

/**
 * ClassName: NIOTest01
 * @Description:
 * @author leegoo
 * @date 2019年06月12日
 */
public class NIOTest01 {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println(byteBuffer.capacity());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.position());

        System.out.println("-----------");
        String buf = "哇啊哈哈好";
        byteBuffer.put(buf.getBytes());
        System.out.println(byteBuffer.capacity());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.position());

        System.out.println("-----------");
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes,0,bytes.length));


    }
}
