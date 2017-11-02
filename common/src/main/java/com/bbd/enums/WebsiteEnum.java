package com.bbd.enums;

import com.google.common.base.Objects;

/**
 * @author liuweibo
 * @version $Id: DistrictEnum.java, v0.1 ${DATA} 17:42 liuweibo Exp $$
 */
public enum WebsiteEnum {

    WHOLE_CITY(1, "新闻"),
    ZHIGUAN(2, "网站"),
    NAMING(3, "微信"),
    YUNYAN(4, "论坛"),
    HUAXI(5, "微博"),
    WUDANG(6, "政务"),
    BAIYUN(7, "其他"),;

    private Integer code;

    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(Integer code) {
        WebsiteEnum[] vals = WebsiteEnum.values();
        for (int i = 0; i < vals.length; i++) {
            if(Objects.equal(vals[i].getCode(), code)) {
                return vals[i].getDesc();
            }
        }
        return null;
    }

    public static Integer getCodeByDesc(String desc) {
        WebsiteEnum[] vals = WebsiteEnum.values();
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
    private WebsiteEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    
}
