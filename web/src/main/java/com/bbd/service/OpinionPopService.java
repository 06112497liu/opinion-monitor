package com.bbd.service;

import com.bbd.domain.PopOpinionMsg;

/**
 * @author Liuweibo
 * @version Id: OpinionPopService.java, v0.1 2018/1/16 Liuweibo Exp $$
 */
public interface OpinionPopService {

    /**
     * 舆情系统弹窗字符串
     * @param userId
     * @param type
     */
    PopOpinionMsg opinionPopupWindowsMsg(Long userId, Integer type);
}
