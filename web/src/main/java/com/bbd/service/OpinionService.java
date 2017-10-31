package com.bbd.service;

import com.bbd.service.vo.WarnOpinionTopTenVo;

import java.util.List;

/**
 * 舆情接口服务
 * @author Liuweibo
 * @version Id: OpinionService.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
public interface OpinionService {

    /**
     * 预警舆情top10
     * @return
     */
    List<WarnOpinionTopTenVo> getWarnOpinionTopTen();
}
