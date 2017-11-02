package com.bbd.enums;

import com.google.common.base.Objects;

/**
 * @author liuweibo
 * @version $Id: DistrictEnum.java, v0.1 ${DATA} 17:42 liuweibo Exp $$
 */
public enum DistrictEnum {
    
    ZHIGUAN("520100", "直管区"),
    NAMING("520102", "南明区"),
    YUNYAN("520103", "云岩区"),
    HUAXI("520111", "花溪区"),
    WUDANG("520112", "乌当区"),
    BAIYUN("520113", "白云区"),
    JINGJI("520114", "经济技术开发区"),
    GUANSHANHU("520115", "观山湖区"),
    KAIYANG("520121", "开阳县"),
    XIFENG("520122", "息烽县"),
    XIUWEN("520123", "修文县"),
    QINGZHEN("520181", "清镇市"),
    HANGKONGGANG("520191", "贵州双龙航空港经济区"),
    BAOSHUI("520192", "贵阳市贵阳综合保税区"),
    GAOXIN("520198", "贵阳国家高新技术产业开发区"),
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
        DistrictEnum[] vals = DistrictEnum.values();
        for (int i = 0; i < vals.length; i++) {
            if(Objects.equal(vals[i].getCode(), code)) {
                return vals[i].getDesc();
            }
        }
        return null;
    }
    
    public static String getCodeByDesc(String desc) {
        DistrictEnum[] vals = DistrictEnum.values();
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
    private DistrictEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    
}
