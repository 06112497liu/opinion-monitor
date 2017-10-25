package com.bbd.enums;

/**
 * @author songyusheng
 * @version $ v 0.1  2017/10/16 14:54 songyusheng Exp $
 */
public enum RegionEnum implements EnumInterface {

    /**
     * 贵阳区域
     */

    ZHIGUAN("520100", "直管区"),

    NANMING("520102", "南明区"),

    YUNYAN("520103", "云岩区"),

    HUAXI("520111", "花溪区"),

    WUDANG("520112", "乌当区"),

    BAIYUN("520113", "白云区"),

    GAOXIN("520114", "经济技术开发区"),

    GUANSHANHU("520115", "观山湖区"),

    KAIYANG("520121", "开阳县"),

    XIFENG("520122", "息烽县"),

    XIUWEN("520123", "修文县"),

    QINGZHEN("520181", "清镇市"),

    SHUANGLONG("520191", "贵州双龙航空港经济区"),

    BAOSHUI("520192", "贵阳市贵阳综合保税区"),

    GAOXINFENJU("520198", "贵阳国家高新技术产业开发区"),

    QITA("52019999", "其他");

    /** 枚举编码 */
    private String code;

    /** 枚举描述 */
    private String desc;

    /**
     * 构造方法
     *
     * @param code
     * @param desc
     */
    RegionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
