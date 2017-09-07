package com.mybatis.domain;

import java.io.Serializable;

/**
 * 分页器，根据page,limit,totalCount用于页面上分页显示多项内容，计算页码和当前页的偏移量，方便页面分页使用.
 * 
 * @author badqiu
 * @author miemiedev
 */
public class Paginator implements Serializable {

    private static final long serialVersionUID = -2429864663690465105L;

    /**
     * 分页大小
     */
    private int               limit;
    /**
     * 页数
     */
    private int               page;
    /**
     * 总记录数
     */
    private int               totalCount;

    public Paginator(int page, int limit, int totalCount) {
        super();
        this.limit = limit;
        this.totalCount = totalCount;
        this.page = page;
    }

    /**
     * 取得当前页。
     */
    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    /**
     * 取得总项数。
     * 
     * @return 总项数
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 得到 总页数
     *
     * @return
     */
    public int getTotalPages() {
        if (totalCount <= 0) {
            return 0;
        }
        if (limit <= 0) {
            return 0;
        }

        int count = totalCount / limit;
        if (totalCount % limit > 0) {
            count++;
        }
        return count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Paginator");
        sb.append("{page=").append(page);
        sb.append(", limit=").append(limit);
        sb.append(", totalCount=").append(totalCount);
        sb.append('}');
        return sb.toString();
    }
}
