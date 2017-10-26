/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.task;

import com.bbd.util.StringUtils;

/**
 * 默认ES的索引。 bbd_opinion_a, bbd_opinion_b
 * @author tjwang
 * @version $Id: IndexOpinionContainer.java, v 0.1 2017/10/26 0026 18:26 tjwang Exp $
 */
public class IndexOpinionContainer {

    public static final String INDEX_ALIAS = "bbd_opinion";
    public static final String INDEX_A     = "bbd_opinion_a";
    public static final String INDEX_B     = "bbd_opinion_b";

    /**
     * 获取新的索引
     * @param curIndex 当前
     * @return
     */
    public static String getNextIndex(String curIndex) {
        if (StringUtils.isBlank(curIndex)) {
            return INDEX_A;
        }
        if (curIndex.equals(INDEX_A)) {
            return INDEX_B;
        }
        return INDEX_A;
    }

}
