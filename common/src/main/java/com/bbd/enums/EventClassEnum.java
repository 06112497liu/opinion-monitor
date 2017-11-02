package com.bbd.enums;

import com.google.common.base.Objects;

/**
 * @author liuweibo
 * @version $Id: DistrictEnum.java, v0.1 ${DATA} 17:42 liuweibo Exp $$
 */
public enum EventClassEnum {

    NAIFEN(1, "奶粉"),
    YINPING(2, "饮品"),
    JIADIAN(3, "家电"),
    DIANSHANG(3, "电商"),
    QICHE(4, "汽车"),
    YIYAO(5, "医药"),
    KUAIDI(6, "快递"),
    CHAOSHI(7, "超市"),
    RIHUA(8, "日化"),
    QITA(9, "其他"),;

    private Integer code;

    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(Integer code) {
        EventClassEnum[] vals = EventClassEnum.values();
        for (int i = 0; i < vals.length; i++) {
            if(Objects.equal(vals[i].getCode(), code)) {
                return vals[i].getDesc();
            }
        }
        return null;
    }

    public static Integer getCodeByDesc(String desc) {
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
    private EventClassEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    
}
