/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.constant;

/**
 * @author tjwang
 * @version $Id: EsConstant.java, v 0.1 2017/10/31 0031 17:13 tjwang Exp $
 */
public class EsConstant {

    public static final String LONG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String IDX_OPINION  = "bbd_opinion_mock_a";
    public static final String OPINION_TYPE = "opinion";

    public static final String IDX_OPINION_OP_RECORD   = "bbd_opinion_op_record";
    public static final String OPINION_OP_RECORD_TYPE  = "opinion_op_record";
    public static final String IDX_OPINION_HOT         = "bbd_opinion_hot";
    public static final String OPINION_HOT_TYPE        = "hot";
    public static final String OPINION_UUID            = "uuid";
    public static final String OPINION_HOT_PROP        = "hot";
    public static final String OPINION_FIRST_WARN_TIME = "firstWarnTime";

    // =========== 舆情操作记录字段 ==========================
    public static final String opOwnerField      = "opOwner";
    public static final String opStatusField     = "opStatus";
    public static final String targeterField     = "targeter";
    public static final String transferTypeField = "transferType";
    public static final String operatorsField    = "operators";
    public static final String removeReasonField = "removeReason";
    public static final String removeNoteField   = "removeNote";
    public static final String opTypeField       = "opType";

    // =========== 舆情详情字段 ==========================
    public static final String uuidField          = "uuid";
    public static final String titleField         = "title";
    public static final String contentField       = "content";
    public static final String publishTimeField   = "publishTime";
    public static final String hotField           = "hot";
    public static final String opTimeField        = "opTime";
    public static final String eventsField        = "events";
    public static final String mediaTypeField     = "mediaType";
    public static final String emotionField       = "emotion";
    public static final String firstWarnTimeField = "firstWarnTime";
    public static final String websiteField       = "website";
    public static final String keywordField       = "keyword";

    // =========== 舆情热度记录字段 ==========================
    public static final String hotTimeField = "hotTime";
}