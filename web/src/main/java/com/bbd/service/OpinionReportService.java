package com.bbd.service;

import com.bbd.service.vo.OpinionExtVO;

import java.io.OutputStream;

/**
 * @author Liuweibo
 * @version Id: OpinionReportService.java, v0.1 2017/12/7 Liuweibo Exp $$
 */
public interface OpinionReportService {

    /**
     * 舆情详情报告
     * @param out
     * @param opinionDetail
     * @return
     */
    void generateDetailReport(OutputStream out, OpinionExtVO opinionDetail);

    /**
     * 预警舆情（日、周、月）报
     * @param out
     * @param type
     */
    void generateStaReport(OutputStream out, Integer type) throws NoSuchFieldException, IllegalAccessException;

}
