package com.bbd.service;

/**
 * @author Liuweibo
 * @version Id: EsInitService.java, v0.1 2017/12/1 Liuweibo Exp $$
 */
public interface EsInitService {

    /**
     * 删除对舆情的opOwner字段
     */
    void delOpOwnerField();

    /**
     * 删除对舆情的operators字段
     */
    void delOperatorsField();

    /**
     * 删除对舆情的transferType字段
     */
    void delTransferTypeField();

    /**
     * 删除对舆情的opStatus字段
     */
    void delOpStatusField();

    /**
     * 删除舆情操作记录索引
     */
    void delOpRecord(String index);
}
