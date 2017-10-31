/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.service.param.OpinionCountStatQueryParam;
import com.bbd.service.vo.OpinionCountStatVO;

/**
 *
 * @author tjwang
 * @version $Id: IndexStatisticService.java, v 0.1 2017/10/31 0031 9:56 tjwang Exp $
 */
public interface IndexStatisticService {

    OpinionCountStatVO getOpinionCountStatistic(OpinionCountStatQueryParam param);

}
