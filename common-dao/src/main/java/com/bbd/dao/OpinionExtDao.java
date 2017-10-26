package com.bbd.dao;


import com.bbd.param.OpinionVo;

import java.util.List;

public interface OpinionExtDao {

     /**
      * 查询预警舆情top10
      * @return
      */
     List<OpinionVo> selectWarnOpinionTopTen();
}