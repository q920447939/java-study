package cn.withmes.springboot.log.desensitization.core;


import cn.hutool.core.util.DesensitizedUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogEncryptFiled {
    DesensitizedUtil.DesensitizedType type();
}
