/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.service.IndexStatisticService;
import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.vo.KeyValueVo;
import com.bbd.service.vo.OpinionCountStatVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author tjwang
 * @version $Id: IndexStatisticEsServiceImpl.java, v 0.1 2017/10/31 0031 17:02 tjwang Exp $
 */
@Service("indexStatisticEsServiceImpl")
public class IndexStatisticEsServiceImpl implements IndexStatisticService {

    @Override
    public OpinionCountStatVO getOpinionCountStatistic(OpinionCountStatQueryParam param) {
        return null;
    }

    @Override
    public Map<String, List<OpinionCountStatVO>> getOpinionCountStatisticGroupTime(OpinionCountStatQueryParam param) {
        return null;
    }

    @Override
    public List<KeyValueVo> getOpinionDBCoordinate() {
        return null;
    }

    @Override
    public Map<String, Object> getSystemSta() {
        return null;
    }

    @Override
    public Map<String, Object> getDBsta() {
        return null;
    }

    @Override
    public List<KeyValueVo> getKeywordsTopTen() {
        return null;
    }

    @Override
    public List<KeyValueVo> getEventChannelTrend() {
        return null;
    }

    @Override
    public List<KeyValueVo> getEventClassTrend() {
        return null;
    }

    @Override
    public List<KeyValueVo> getEventAreaTrend() {
        return null;
    }
}
