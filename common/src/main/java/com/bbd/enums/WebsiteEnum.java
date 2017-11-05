package com.bbd.enums;

import com.google.common.base.Objects;

/**
 * @author liuweibo
 * @version $Id: DistrictEnum.java, v0.1 ${DATA} 17:42 liuweibo Exp $$
 */
public enum WebsiteEnum {

    XINWEN("1", "新闻"),
    WANGZHAN("2", "网站"),
    WEIXIN("3", "微信"),
    LUNTAN("4", "论坛"),
    WEIBO("5", "微博"),
    JIAOWU("6", "政务"),
    QITA("7", "其他"),;

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        WebsiteEnum[] vals = WebsiteEnum.values();
        for (int i = 0; i < vals.length; i++) {
            if(Objects.equal(vals[i].getCode(), code)) {
                return vals[i].getDesc();
            }
        }
        return null;
    }

    public static String getCodeByDesc(String desc) {
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
    private WebsiteEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    
}
