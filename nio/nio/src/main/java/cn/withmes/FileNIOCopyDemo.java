/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月01日
 */
package cn.withmes;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ClassName: FileNIOCopyDemo
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月01日
 */
public class FileNIOCopyDemo {
    static Logger log = Logger.getLogger(UseBuffer.class.getName());
    static String sourcePath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
    static String srcDecodePath = IOUtil.getResourcePath(sourcePath);

    public static void main(String[] args) {
//演示复制资源文件
        nioCopyResouceFile();
    }

    /**
     * 复制两个资源目录下的文件
     */
    public static void nioCopyResouceFile() {
//源
        String sourcePath =
                NioDemoConfig.FILE_RESOURCE_SRC_PATH;
        String srcPath = IOUtil.getResourcePath(sourcePath);
        log.info("srcPath=" + srcPath);
//目标
        String destPath =
                NioDemoConfig.FILE_RESOURCE_DEST_PATH;
        String destDecodePath =
                IOUtil.builderResourcePath(destPath);
        log.info("destDecodePath=" + destDecodePath);
//复制文件
        nioCopyFile(srcDecodePath, destDecodePath);
    }

    /**
     * NIO方式复制文件
     *
     * @param srcPath  源路径
     * @param destPath 目标路径
     */
    public static void nioCopyFile(String srcPath, String
            destPath) {
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        try {
//如果目标文件不存在，则新建
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            long startTime = System.currentTimeMillis();
            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel inChannel = null; //输入通道
            FileChannel outchannel = null; //输出通道
            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                inChannel = fis.getChannel();
                outchannel = fos.getChannel();
                int length = -1;
                //新建buf，处于写模式
                ByteBuffer buf = ByteBuffer.allocate(1024);
                //从输入通道读取到buf
                while ((length = inChannel.read(buf)) != -1) {
                    //buf第一次模式切换：翻转buf，从写模式变成读模式
                    buf.flip();
                    int outlength = 0;
                    //将buf写入输出的通道
                    while ((outlength = outchannel.write(buf)) !=
                            0) {
                        System.out.println("写入的字节数：" +
                                outlength);
                    }
                    //buf第二次模式切换：清除buf，变成写模式
                    buf.clear();
                }
                //强制刷新到磁盘
                outchannel.force(true);
            } finally {
                //关闭所有的可关闭对象
                IOUtil.closeQuietly(outchannel);
                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(inChannel);
                IOUtil.closeQuietly(fis);
            }
            long endTime = System.currentTimeMillis();
            log.info("base复制毫秒数：" + (endTime - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
