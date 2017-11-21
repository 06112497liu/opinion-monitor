package com.bbd.enums;

import com.google.common.base.Objects;

/**
 * @author liuweibo
 * @version $Id: DistrictEnum.java, v0.1 ${DATA} 17:42 liuweibo Exp $$
 */
public enum DistrictExtEnum {

    GUIYANG("5201", "贵阳市工商局"),
    ZHIGUAN("520100", "直管区工商局"),
    NAMING("520102", "南明区工商局"),
    YUNYAN("520103", "云岩区工商局"),
    HUAXI("520111", "花溪区工商局"),
    WUDANG("520112", "乌当区工商局"),
    BAIYUN("520113", "白云区工商局"),
    JINGJI("520114", "经济技术开发区工商局"),
    GUANSHANHU("520115", "观山湖区工商局"),
    KAIYANG("520121", "开阳县工商局"),
    XIFENG("520122", "息烽县工商局"),
    XIUWEN("520123", "修文县工商局"),
    QINGZHEN("520181", "清镇市工商局"),
    HANGKONGGANG("520191", "贵州双龙航空港经济区工商局"),
    BAOSHUI("520192", "贵阳市贵阳综合保税区工商局"),
    GAOXIN("520198", "贵阳国家高新技术产业开发区工商局"),
    QITA("52019999", "其他"),;

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        DistrictExtEnum[] vals = DistrictExtEnum.values();
        for (int i = 0; i < vals.length; i++) {
            if(Objects.equal(vals[i].getCode(), code)) {
                return vals[i].getDesc();
            }
        }
        return null;
    }

    public static String getCodeByDesc(String desc) {
        DistrictExtEnum[] vals = DistrictExtEnum.values();
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
    private DistrictExtEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    
}
