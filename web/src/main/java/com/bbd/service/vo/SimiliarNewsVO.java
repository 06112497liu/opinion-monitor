package com.bbd.service.vo;

import com.bbd.util.StringUtils;

/**
 * 相同文章VO
 * @author Liuweibo
 * @version Id: SameArticleVO.java, v0.1 2017/11/1 Liuweibo Exp $$
 */
public class SimiliarNewsVO {

    /**
     * 标题
     */
    private String  title;

    /**
     * 网站
     */
    private String website;

    /**
     * 来源
     */
    private String source;

    /**
     * 真实来源
     */
    private String realSource;

    /**
     * 热度
     */
    private Integer hot;

    /**
     * 链接
     */
    private String link;


    public SimiliarNewsVO() {
    }

    public SimiliarNewsVO(String title, String source, Integer hot) {
        this.title = title;
        this.source = source;
        this.hot = hot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRealSource() {
        if (StringUtils.isNotEmpty(website)) return website;
        if (StringUtils.isNotEmpty(source)) return source;
        return "";
    }

    public void setRealSource(String realSource) {
        this.realSource = realSource;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
    
    