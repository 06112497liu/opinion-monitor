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
 * @author Liuweibo
 * @version Id: IndexStatisticServiceImpl.java, v0.1 2017/11/2 Liuweibo Exp $$
 */
@Service("indexStatisticDBServiceImpl")
public class IndexStatisticDBServiceImpl implements IndexStatisticService {

    @Override
    public OpinionCountStatVO getOpinionCountStatistic(Integer state, Integer timeSpan) {

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
    
    