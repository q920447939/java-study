/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年12月21日
 */
package cn.withmes.springboot.log.desensitization.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Plugin(name = "MaskSensitiveDataPolicy", category = Core.CATEGORY_NAME,
        elementType = "rewritePolicy", printObject = true)
public class MaskSensitiveDataPolicy implements RewritePolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaskSensitiveDataPolicy.class);


    private String[] sensitiveClasses;
    private volatile boolean encryptEnable;


    @PluginFactory
    public static MaskSensitiveDataPolicy createPolicy(
            @PluginElement("sensitive") final String[] sensitiveClasses) {
        return new MaskSensitiveDataPolicy(sensitiveClasses);
    }

    private MaskSensitiveDataPolicy(String[] sensitiveClasses) {
        super();
        //在这里 需要手动或许系统参数。因为我通过从spring那里获取，发现log框架加载比spring 快。导致取不到数据或者空指针 不过影响不大。自己取也是一回事
        encryptEnable = Boolean.parseBoolean(System.getProperty("log4j2.encrypt.enable"));
        this.sensitiveClasses = sensitiveClasses;
    }

    @Override
    public LogEvent rewrite(LogEvent event) {
        Message rewritten = rewriteIfSensitive(event.getMessage());
        if (rewritten != event.getMessage()) {
            return new Log4jLogEvent.Builder(event).setMessage(rewritten).build();
        }
        return event;
    }

    private Message rewriteIfSensitive(Message message) {
        // 确保已经通过设置系统属性`log4j2.enable.threadlocals` 为 `false`关闭了garbage-free logging
        // 否则可能传入ReusableObjectMessage, ReusableParameterizedMessage或
        // MutableLogEvent messages 导致不能重写。

        // Make sure to switch off garbage-free logging
        // by setting system property `log4j2.enable.threadlocals` to `false`.
        // Otherwise you may get ReusableObjectMessage, ReusableParameterizedMessage
        // or MutableLogEvent messages here which may not be rewritable...
        if (message instanceof ObjectMessage) {
            return rewriteObjectMessage((ObjectMessage) message);
        }
        if (message instanceof ParameterizedMessage) {
            return rewriteParameterizedMessage((ParameterizedMessage) message);
        }
        return message;
    }

    private Message rewriteObjectMessage(ObjectMessage message) {
        SensitiveStrategy sensitive = isSensitive(message.getParameter());
        if (encryptEnable && sensitive.coincidence()) {
            return new ObjectMessage(sensitive.strategy(message.getParameter()));
        }
        return message;
    }

    private Message rewriteParameterizedMessage(ParameterizedMessage message) {
        Object[] params = message.getParameters();
        boolean changed = rewriteSensitiveParameters(params);
        return changed && encryptEnable ? new ParameterizedMessage(message.getFormat(), params) : message;
    }

    private boolean rewriteSensitiveParameters(Object[] params) {
        boolean changed = false;
        for (int i = 0; i < params.length; i++) {
            SensitiveStrategy sensitive = isSensitive(params[i]);
            if (sensitive.coincidence()) {
                params[i] = sensitive.strategy(params[i]);
                changed = true;
            }
        }
        return changed;
    }

    /**
     * @param parameter 入参
     * @return cn.withmes.springboot.log.desensitization.core.MaskSensitiveDataPolicy.SensitiveStrategy
     * @Description 根据入参进行判断是否需要进行脱敏
     * 注意:
     * 1.如果日志在打印的时候，使用了json.toString 这种。 那么parameter的类型是String. 对于这种类型就像list类型一样，没有很好的办法，只能每个属性进行遍历
     * 目前这个方法只匹配了对象类型和List类型的数据
     * @auther liming
     * @date: 2021/12/22 14:01
     */
    private SensitiveStrategy isSensitive(Object parameter) {
        //默认返回的是不进行脱敏
        SensitiveStrategy defaultStrategy = new SensitiveStrategy() {
            @Override
            public boolean coincidence() {
                return false;
            }

            @Override
            public Object strategy(Object params) {
                return null;
            }
        };
        if (null == parameter) return defaultStrategy;
        // TODo 加入缓存，可减少每次的判断
        LogEncryptClz logEncryptClz = parameter.getClass().getAnnotation(LogEncryptClz.class);
        boolean objInfer = null != logEncryptClz && logEncryptClz.enable() && parameter.getClass().getPackage().getName().startsWith("cn.withmes");
        if (objInfer) {
            return new ObjectSensitiveStrategy(true);
        }
        if (parameter instanceof List) {
            return new ListSensitiveStrategy(true);
        }
        return defaultStrategy;
    }


    public interface SensitiveStrategy {
        boolean coincidence();

        Object strategy(Object params);
    }

    public static class ObjectSensitiveStrategy implements SensitiveStrategy {

        private final boolean coincidence;

        public ObjectSensitiveStrategy(boolean coincidence) {
            this.coincidence = coincidence;
        }

        @Override
        public boolean coincidence() {
            return this.coincidence;
        }

        @Override
        public Object strategy(Object parameter) {
            if (null == parameter) {
                return null;
            }
            return objectStrategy(parameter);
        }

        public static Object objectStrategy(Object parameter) {
            JSONObject json = (JSONObject) JSONObject.toJSON(parameter);
            Map<String, Field> fileNameMaps = Stream.of(ReflectUtil.getFields(parameter.getClass())).filter(field -> {
                return field.isAnnotationPresent(LogEncryptFiled.class);
            }).collect(Collectors.toMap(Field::getName, Function.identity(), (a, b) -> b));
            if (MapUtil.isEmpty(fileNameMaps)) {
                return parameter;
            }

            for (String key : json.keySet()) {
                Object v = json.get(key);
                if (null == v || !fileNameMaps.containsKey(key)) {
                    continue;
                }

                Field field = fileNameMaps.get(key);
                LogEncryptFiled annotation = field.getAnnotation(LogEncryptFiled.class);
                DesensitizedUtil.DesensitizedType desensitizedType = annotation.type();
                try {
                    json.put(key, DesensitizedUtil.desensitized(String.valueOf(json.get(key)), desensitizedType));
                } catch (Exception exception) {
                    LOGGER.error("【数据脱敏】处理脱敏数据异常 源数据={}", json, exception);
                }
            }
            return JSONObject.toJSON(json);
        }
    }

    public static class ListSensitiveStrategy implements SensitiveStrategy {

        private final boolean coincidence;

        public ListSensitiveStrategy(boolean coincidence) {
            this.coincidence = coincidence;
        }

        @Override
        public boolean coincidence() {
            return this.coincidence;
        }

        @Override
        public Object strategy(Object parameter) {
            if (null == parameter) {
                return null;
            }
            List list = (List) parameter;
            if (CollUtil.isEmpty(list)) return null;

            List result = new ArrayList();
            for (Object o : list) {
                LogEncryptClz annotation = o.getClass().getAnnotation(LogEncryptClz.class);
                if (null == annotation || !annotation.enable() || !o.getClass().getPackage().getName().startsWith("cn.withmes")) {
                    continue;
                }

                if (o instanceof List) {
                    //这里可能对象里面包含list元素，所以要用递归
                    result.add(strategy(o));
                    continue;
                }
                result.add(ObjectSensitiveStrategy.objectStrategy(o));
            }


            return result;
        }
    }


}
