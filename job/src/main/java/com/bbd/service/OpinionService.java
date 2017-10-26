/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.dao.OpinionDao;
import com.bbd.domain.Opinion;
import com.bbd.domain.OpinionExample;
import com.bbd.service.vo.OpinionEsVO;
import com.bbd.util.BeanMapperUtil;
import com.bbd.util.StringUtils;
import com.mybatis.domain.PageBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 舆情服务
 * @author tjwang
 * @version $Id: OpinionService.java, v 0.1 2017/10/26 0026 10:16 tjwang Exp $
 */
@Service
public class OpinionService {

    @Autowired
    private OpinionDao opinionDao;

    public List<OpinionEsVO> queryOpinion() {
        OpinionExample exam = new OpinionExample();
        List<Opinion> ds = opinionDao.selectByExample(exam);
        return BeanMapperUtil.mapList(ds, OpinionEsVO.class);
    }

    /**
     * 查询大于指定UUID的数据
     * @param uuid
     * @param pb
     * @return
     */
    public List<OpinionEsVO> queryOpinionLessThanUUID(String uuid, PageBounds pb) {
        OpinionExample exam = new OpinionExample();
        if (StringUtils.isNotBlank(uuid)) {
            exam.createCriteria().andUuidLessThan(uuid);
        }
        exam.setOrderByClause("uuid desc");
        List<Opinion> ds = opinionDao.selectByExampleWithPageBounds(exam, pb);
        return BeanMapperUtil.mapList(ds, OpinionEsVO.class);
    }
}
