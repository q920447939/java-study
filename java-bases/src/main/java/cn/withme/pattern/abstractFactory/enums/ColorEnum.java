package cn.withme.pattern.abstractFactory.enums;

/**
 * @Description: 颜色枚举类
 * @Author: liming
 * @date 2020年01月16日
 */
public enum ColorEnum {
    RED("red","红色"),
    GREEN("green","绿色"),
    ;

    private String code;
    private String desc ;

    ColorEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getTypeNameByType (String type){
        for (ColorEnum c : ColorEnum.values()) {
            if (c.getCode().equalsIgnoreCase(type)) {
                return c.getDesc();
            }
        }
        return null;
    }
}
