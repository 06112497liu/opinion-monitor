package com.bbd.service.vo;

/**
 * @author Liuweibo
 * @version Id: OpinionExtVo.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
public class OpinionExtVO extends OpinionVO {

    /**
     * 舆情内容
     */
    private String content;

    /**
     * 关键词：满足爬虫搜索的词
     */
    private String[] keys;

    /**
     * 词云
     */
    private String[] keyword;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public String[] getKeyword() {
        return keyword;
    }

    public void setKeyword(String[] keyword) {
        this.keyword = keyword;
    }
}
    
    