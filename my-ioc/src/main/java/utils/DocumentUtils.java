/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package utils;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * ClassName: DocumentUtils
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public class DocumentUtils {

    public  static org.dom4j.Document createDocument(InputStream is){
        SAXReader reader   = new SAXReader ();
        //  读取xml文件
        try {
           return  reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
