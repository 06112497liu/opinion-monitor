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

    public static final String LONG_TIME_FORMAT               = "yyyy-MM-dd HH:mm:ss";

    // =========== elasitcsearch 索引类型信息 ==================================
    public static final String IDX_OPINION                    = "bbd_opinion";
    public static final String OPINION_TYPE                   = "opinion";

    public static final String IDX_OPINION_HOT                = "bbd_opinion_hot";
    public static final String OPINION_HOT_TYPE               = "hot";

    public static final String IDX_OPINION_SIMILAR_NEWS       = "bbd_opinion_news";
    public static final String OPINION_SIMILAR_NEWS_TYPE      = "opinion_news";

    public static final String IDX_OPINION_OP_RECORD          = "bbd_opinion_op_record";
    public static final String OPINION_OP_RECORD_TYPE         = "opinion_op_record";

    public static final String OPINION_UUID                   = "uuid";
    public static final String OPINION_HOT_PROP               = "hot";
    public static final String OPINION_OPSTATUS_PROP          = "opStatus";

    public static final String OPINION_FIRST_WARN_TIME        = "warnTime.firstWarnTime";
    public static final String OPINION_FIRST_WARN_TIME_ONE    = "warnTime.firstWarnTimeOne";
    public static final String OPINION_FIRST_WARN_TIME_TWO    = "warnTime.firstWarnTimeTwo";
    public static final String OPINION_FIRST_WARN_TIME_THREE  = "warnTime.firstWarnTimeThree";

    // =========== 舆情操作记录字段 ==========================
    public static final String opOwnerField                   = "opOwner";
    public static final String opStatusField                  = "opStatus";
    public static final String targeterField                  = "targeter";
    public static final String transferTypeField              = "transferType";
    public static final String operatorField                 = "operator";
    public static final String removeReasonField              = "removeReason";
    public static final String removeNoteField                = "removeNote";
    public static final String opTypeField                    = "opType";
    public static final String opTimeField                    = "opTime";

    // =========== 舆情详情字段 ==========================
    public static final String uuidField                      = "uuid";
    public static final String titleField                     = "title";
    public static final String contentField                   = "content";
    public static final String publishTimeField               = "publishTime";
    public static final String hotField                       = "hot";
    public static final String eventsField                    = "events";
    public static final String mediaTypeField                 = "mediaType";
    public static final String emotionField                   = "emotion";
    public static final String warnTimeField                  = "warnTime";
    public static final String websiteField                   = "website";
    public static final String keywordField                   = "keywords";
    public static final String keysField                      = "keys";
    public static final String operatorsField                 = "operators";
    public static final String recordTimeField                = "recordTime";
    public static final String levelField                     = "level";

    // =========== 舆情热度记录字段 ==========================
    public static final String hotTimeField                   = "hotTime";

    // =========== 相同文章信息字段 ==========================
    public static final String opinionIDField                 = "opinion_id";

    // =========== 舆情、事件关联记录 ========================
    public static final String IDX_OPINION_EVENT_RECORD       = "bbd_opinion_event_record";
    /**
     * 舆情与事件首次关联时间
     */
    public static final String OPINION_EVENT_RECORD_TYPE      = "opinion_event_record";
    /**
     * 舆情热度超过事件预警时间
     */
    public static final String OPINION_EVENT_RECORD_HOT_TYPE  = "opinion_event_hot_record";
    /**
     * 舆情热度超过舆情预警时间
     */
    public static final String OPINION_EVENT_RECORD_WARN_TYPE = "opinion_event_warn_record";
    public static final String eventIdField                   = "eventId";
    public static final String matchTimeField                 = "matchTime";
    public static final String matchTimeTrimField             = "matchTimeTrim";
    public static final String opinionIdField                 = "opinionId";
}