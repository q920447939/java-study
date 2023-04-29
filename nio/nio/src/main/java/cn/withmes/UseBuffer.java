/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月01日
 */
package cn.withmes;

import org.apache.log4j.Logger;

import java.nio.IntBuffer;

/**
 * ClassName: UseBuffer
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月01日
 */
public class UseBuffer {
    static Logger log = Logger.getLogger(UseBuffer.class.getName());

    //一个整型的Buffer静态变量
    static IntBuffer intBuffer = null;

    public static void allocateTest() {
        //创建一个intBuffer实例对象
        intBuffer = IntBuffer.allocate(20);
        log.debug("------------after allocate------------------");
        log.debug("position=" +
                intBuffer.position());
        log.debug("limit=" + intBuffer.limit());
        log.debug("capacity=" +
                intBuffer.capacity());
    }

    public static void put() {
        for (int i = 0; i < 5; i++) {
            //写入一个整数到缓冲区
            intBuffer.put(i);
        }
        //输出缓冲区的主要属性值
        log.debug("------------after putTest------------------");
        log.debug("position=" + intBuffer.position());
        log.debug("limit=" + intBuffer.limit());
        log.debug("capacity=" + intBuffer.capacity());
    }

    public static void flipTest() {
        //翻转缓冲区，从写模式翻转成读模式
        intBuffer.flip();
        //输出缓冲区的主要属性值
        log.info("------------after flip ------------------");
        log.info("position=" +
                intBuffer.position());
        log.info("limit=" +
                intBuffer.limit());
        log.info("capacity=" +
                intBuffer.capacity());
    }

    public static void getTest() {
        //先读2个数据
        for (int i = 0; i < 2; i++) {
            int j = intBuffer.get();
            log.info("j = " + j);
        }
        //输出缓冲区的主要属性值
        log.info("---------after get 2 int --------------");
        log.info("position=" +
                intBuffer.position());
        log.info("limit=" + intBuffer.limit());
        log.info("capacity=" +
                intBuffer.capacity());
        //再读3个数据
        for (int i = 0; i < 3; i++) {
            int j = intBuffer.get();
            log.info("j = " + j);
        }
        //输出缓冲区的主要属性值
        log.info("---------after get 3 int --------- ------");
        log.info("position=" +
                intBuffer.position());
        log.info("limit=" + intBuffer.limit());
        log.info("capacity=" +
                intBuffer.capacity());
    }

    public static void rewindTest() {
        //倒带
        intBuffer.rewind();
        //输出缓冲区属性
        log.info("------------after rewind ------------------");
        log.info("position=" +
                intBuffer.position());
        log.info("limit=" + intBuffer.limit());
        log.info("capacity=" +
                intBuffer.capacity());
    }

    public static void reRead() {
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                //临时保存，标记一下第3个位置
                intBuffer.mark();
            }
            //读取元素
            int j = intBuffer.get();
            log.info("j = " + j);
        }
        //输出缓冲区的属性值
        log.info("------------after reRead------------------");
        log.info("position=" +
                intBuffer.position());
        log.info("limit=" + intBuffer.limit());
        log.info("capacity=" +
                intBuffer.capacity());
    }

    public static void afterReset() {
        log.info("------------after reset------------------");
        //把前面保存在mark中的值恢复到position中
        intBuffer.reset();
        //输出缓冲区的属性值
        log.info("position=" +
                intBuffer.position());
        log.info("limit=" + intBuffer.limit());
        log.info("capacity=" +
                intBuffer.capacity());
        //读取并且输出元素
        for (int i = 2; i < 5; i++) {
            int j = intBuffer.get();
            log.info("j = " + j);
        }
    }

    public static void clearDemo() {
        log.info("------------after clear------------------");
        //清空缓冲区，进入写模式
        intBuffer.clear();
        //输出缓冲区的属性值
        log.info("position=" + intBuffer.position());
        log.info("limit=" + intBuffer.limit());
        log.info("capacity=" + intBuffer.capacity());
    }

    public static void main(String[] args) {
        allocateTest();
        put();
        flipTest();
        getTest();
        rewindTest();
        reRead();
        afterReset();
        clearDemo();
    }
}
