package cn.withme.pattern.abstractFactory.enums;

/**
 * @Description: 位置枚举类
 * @Author: liming
 * @date 2020年01月16日
 */
public enum LocationEnum {
    CHANGSHA("changsha","长沙"),
    GUANGZHOU("guangzhou","广州"),
    ;

    private String code;
    private String desc ;

    LocationEnum(String code, String desc) {
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
        for (LocationEnum c : LocationEnum.values()) {
            if (c.getCode().equalsIgnoreCase(type)) {
                return c.getDesc();
            }
        }
        return null;
    }
}
