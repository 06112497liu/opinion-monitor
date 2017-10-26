package com.bbd.dao;


import com.bbd.param.ChannelTrend;
import com.bbd.param.NameValueInfo;
import com.bbd.param.OpinionVo;

import java.util.List;
import java.util.Map;

public interface OpinionExtDao {

     /**
      * 查询预警舆情top10
      * @return
      */
     List<OpinionVo> selectWarnOpinionTopTen();

     /**
      * 查询不同时间段舆情增量
      * @return
      */
     Map<String, Long> selectAddOpinionGroupByTime();

     /**
      * 查询舆情传播渠道分布
      * @return
      */
     List<ChannelTrend> selectOpinionChannelTrend();

     /**
      * 查询舆情事件类型分布
      * @return
      */
     List<NameValueInfo> selectEventTypeTrend();

     /**
      * 查询舆情事件地域分布
      * @return
      */
     List<NameValueInfo> selectEventMapTrend();


}