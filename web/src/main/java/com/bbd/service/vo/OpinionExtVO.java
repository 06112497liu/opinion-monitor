package com.bbd.service.vo;

import com.bbd.domain.SimiliarNews;

import java.util.List;

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
     * 词云
     */
    private String keyword;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "OpinionExtVO{" +
                "content='" + content + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
    
    