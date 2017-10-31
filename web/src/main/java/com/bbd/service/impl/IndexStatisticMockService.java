/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service.impl;

import com.bbd.service.IndexStatisticService;
import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.vo.OpinionCountStatVO;
import org.springframework.stereotype.Service;

/**
 * 首页统计Mock服务
 * @author tjwang
 * @version $Id: IndexStatisticMockService.java, v 0.1 2017/10/31 0031 9:56 tjwang Exp $
 */
@Service("indexStatisticMockService")
public class IndexStatisticMockService implements IndexStatisticService {

    @Override
    public OpinionCountStatVO getOpinionCountStatistic(OpinionCountStatQueryParam param) {
        OpinionCountStatVO r = new OpinionCountStatVO();
        r.setTotal(1000);
        r.setLevelOne(20);
        r.setLevelTwo(100);
        r.setLevelThree(880);
        return r;
    }

}
