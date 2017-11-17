package com.bbd.service;

import java.util.Map;

/**
 * @author Liuweibo
 * @version Id: DictionaryService.java, v0.1 2017/11/17 Liuweibo Exp $$
 */
public interface DictionaryService {

    /**
     * 字典表查询
     * @param type A-(奶粉、食品等项里列表); B-(工商局，其他列表查询); C-(区域查询); D-(级别查询); E-(时间状态查询); F-(媒体类型查询); G-(转发内容查询); H-(情感查询); I-(工商局列表)
     * @return
     */
    Map<String, String> queryDictionary(String type);

}
    
    