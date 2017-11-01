/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.service.IndexStatisticService;
import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.vo.DBStaVO;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionCountStatVO;
import com.bbd.service.vo.SystemStaVO;
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
    public Map<String, List<KeyValueVO>> getOpinionCountStatisticGroupTime(OpinionCountStatQueryParam param) {
        return null;
    }

    @Override
    public List<KeyValueVO> getOpinionDBCoordinate() {
        return null;
    }

    @Override
    public SystemStaVO getSystemSta() {
        return null;
    }

    @Override
    public DBStaVO getDBsta() {
        return null;
    }

    @Override
    public List<KeyValueVO> getKeywordsTopTen() {
        return null;
    }

    @Override
    public List<KeyValueVO> getEventChannelTrend() {
        return null;
    }

    @Override
    public List<KeyValueVO> getEventClassTrend() {
        return null;
    }

    @Override
    public List<KeyValueVO> getEventAreaTrend() {
        return null;
    }
}
