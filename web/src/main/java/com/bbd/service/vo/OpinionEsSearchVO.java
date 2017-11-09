/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.vo;

import com.bbd.bean.OpinionEsVO;

import java.util.List;

/**
 * 舆情ES查询结果
 * @author tjwang
 * @version $Id: OpinionEsSearchVO.java, v 0.1 2017/11/1 0001 14:11 tjwang Exp $
 */
public class OpinionEsSearchVO {

    private Long              total;

    private List<OpinionEsVO> opinions;

    private List<KeyValueVO>  hotLevelStats;

    private List<KeyValueVO>  mediaTypeStats;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<OpinionEsVO> getOpinions() {
        return opinions;
    }

    public void setOpinions(List<OpinionEsVO> opinions) {
        this.opinions = opinions;
    }

    public List<KeyValueVO> getHotLevelStats() {
        return hotLevelStats;
    }

    public void setHotLevelStats(List<KeyValueVO> hotLevelStats) {
        this.hotLevelStats = hotLevelStats;
    }

    public List<KeyValueVO> getMediaTypeStats() {
        return mediaTypeStats;
    }

    public void setMediaTypeStats(List<KeyValueVO> mediaTypeStats) {
        this.mediaTypeStats = mediaTypeStats;
    }
}
