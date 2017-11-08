package com.bbd.service.param;

/**
 * @author Liuweibo
 * @version Id: OpOpinionRecordParam.java, v0.1 2017/11/8 Liuweibo Exp $$
 */
public class OpOpinionRecordParam {

    private String uuid; 			// 舆情号
    private Integer opType;			// 操作类型, 1. 转发；2. 归档；3. 监测
    private String operator;		// 操作者
    private String targeter;		// 目标对象
    private String opTime;			// 操作时间

    private Integer removeReason;   // 解除理由, 1. 非敏感舆情；2. 非消费舆情； 3. 非职能范围； 4. 已处理同类舆情
    private String removeNote;		// 解除备注

}
    
    