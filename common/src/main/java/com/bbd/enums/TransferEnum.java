package com.bbd.enums;

import com.google.common.base.Objects;

/**
 * @author Liuweibo
 * @version Id: TransferEnum.java, v0.1 2017/11/8 Liuweibo Exp $$
 */
public enum TransferEnum {

    QINGSHI_ONE("1", "请示该条舆情是否添加为监测事件？"),
    QINGSHI_TWO("2", "请核查并处理该条舆情信息"),
    QINGSHI_THREE("3", "确定该条舆情添加为监测事件，请执行"),
    HUIFU_ONE("4", "经分析，请暂缓处理该条舆情信息"),
    HUIFU_TWO("5", "经分析，请解除该条舆情预警"),
    HUIFU_THREE("6", "经分析，请转发给其他处室处理");

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        TransferEnum[] vals = TransferEnum.values();
        for (int i = 0; i < vals.length; i++) {
            if(Objects.equal(vals[i].getCode(), code)) {
                return vals[i].getDesc();
            }
        }
        return null;
    }

    public static String getCodeByDesc(String desc) {
        TransferEnum[] vals = TransferEnum.values();
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
    private TransferEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
    
    