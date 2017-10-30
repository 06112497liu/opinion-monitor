package com.bbd.param;

/**
 * @author Liuweibo
 * @version Id: ChannelTrend.java, v0.1 2017/10/26 Liuweibo Exp $$
 */
public class ChannelTrend {

    private Integer sourceType;

    private String sourceTypeDesc;

    private Long num;

    private Double percent;

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceTypeDesc() {
        return sourceTypeDesc;
    }

    public void setSourceTypeDesc(String sourceTypeDesc) {
        this.sourceTypeDesc = sourceTypeDesc;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "ChannelTrend{" +
                "sourceType=" + sourceType +
                ", sourceTypeDesc='" + sourceTypeDesc + '\'' +
                ", num=" + num +
                ", percent=" + percent +
                '}';
    }
}
    
    