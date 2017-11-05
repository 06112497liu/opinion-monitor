package com.bbd.enums;

import com.google.common.base.Objects;

/**
 * @author liuweibo
 * @version $Id: DistrictEnum.java, v0.1 ${DATA} 17:42 liuweibo Exp $$
 */
public enum EventClassEnum {

    NAIFEN("1", "奶粉"),
    SHIPING("2", "食品"),
    YINPING("3", "饮品"),
    JIADIAN("4", "家电"),
    DIANSHANG("5", "电商"),
    QICHE("6", "汽车"),
    YIYAO("7", "医药"),
    KUAIDI("8", "快递"),
    CHAOSHI("9", "超市"),
    RIHUA("10", "日化"),
    QITA("11", "其他"),;

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        EventClassEnum[] vals = EventClassEnum.values();
        for (int i = 0; i < vals.length; i++) {
            if(Objects.equal(vals[i].getCode(), code)) {
                return vals[i].getDesc();
            }
        }
        return null;
    }

    public static String getCodeByDesc(String desc) {
        EventClassEnum[] vals = EventClassEnum.values();
        for (int i = 0; i < vals.length; i++) {
            if(Objects.equal(vals[i].getDesc(), desc)) {
                return vals[i].getCode();
            }
        }
        return null;
    }

    /**
     * @param code
     * @param desc
     */
    private EventClassEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    
}
