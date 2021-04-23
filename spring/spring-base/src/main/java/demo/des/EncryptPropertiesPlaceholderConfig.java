/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年04月23日
 */
package demo.des;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ClassName: EncryptPropertiesPlaceholderConfig
 *
 * @author leegoo
 * @Description:
 * @date 2021年04月23日
 */
@Component
public class EncryptPropertiesPlaceholderConfig extends PropertyPlaceholderConfigurer {
    private String[] encryptProNames = new String[]{"name"};


    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (Arrays.asList(encryptProNames).contains(propertyName)) {
            return DESUtils.getDecryptString(propertyValue);
        }
        return super.convertProperty(propertyName, propertyValue);
    }

}
