package com.bbd.enums;

import com.google.common.base.Objects;

/**
 * @author Liuweibo
 * @version Id: TransferEnum.java, v0.1 2017/11/8 Liuweibo Exp $$
 */
public enum WarnReasonEnum {

    QINGSHI_ONE("1", "非敏感舆情"),
    QINGSHI_TWO("2", "非消费舆情"),
    QINGSHI_THREE("3", "非职能范围"),
    HUIFU_ONE("4", "已处理同类舆情");

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        WarnReasonEnum[] vals = WarnReasonEnum.values();
        for (int i = 0; i < vals.length; i++) {
            if(Objects.equal(vals[i].getCode(), code)) {
                return vals[i].getDesc();
            }
        }
        return null;
    }

    public static String getCodeByDesc(String desc) {
        WarnReasonEnum[] vals = WarnReasonEnum.values();
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
    private WarnReasonEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
    
    