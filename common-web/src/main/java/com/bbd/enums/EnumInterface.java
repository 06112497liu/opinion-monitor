package com.bbd.enums;

/**
 * @author songyusheng
 * @version $ v 0.1  2017/10/16 15:00 songyusheng Exp $
 */
public interface EnumInterface {

    /**
     * 获取枚举中定义的值
     *
     * @return 枚举代码
     */
    public String getCode();

    /**
     * 获取枚举中定义的备注信息
     *
     * @return 枚举代码描述
     */
    public String getDesc();
}
