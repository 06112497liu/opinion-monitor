package com.bbd.service;

import com.bbd.annotation.TimeUsed;
import com.bbd.dao.OpinionDao;
import com.bbd.dao.OpinionStatusDao;
import com.bbd.dao.SimiliarNewsDao;
import com.bbd.domain.*;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.BizErrorCode;
import com.bbd.exception.CommonErrorCode;
import com.bbd.param.OpinionInfo;
import com.bbd.util.BeanMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Liuweibo
 * @version Id: OpinionService.java, v0.1 2017/10/27 Liuweibo Exp $$
 */
@Service
public class OpinionService {

    @Autowired
    private OpinionDao opinionDao;

    @Autowired
    private SimiliarNewsDao newsDao;

    @Autowired
    private SystemSettingService settingService;

    @Autowired
    private OpinionStatusDao opinionStatusDao;

    /**
     * 根据uuid查询舆情详情
     * @param uuid
     * @return
     */
    @TimeUsed
    public OpinionInfo getOpinionDetail(String uuid) {
        // step-1：查询舆情信息
        OpinionExample example = new OpinionExample();
        example.createCriteria().andUuidEqualTo(uuid);
        List<Opinion> temp = opinionDao.selectByExample(example);
        if(temp.isEmpty()) return null;

        // step-2：选出同步序号最大的舆情信息
        Optional<Opinion> op = temp.stream().max(Comparator.comparingInt(Opinion::getSeq));

        // step-3：查询相同文章信息
        Opinion maxSeq = op.get();
        Integer seq = maxSeq.getSeq();
        SimiliarNewsExample ex = new SimiliarNewsExample();
        ex.createCriteria().andSeqEqualTo(seq);
        List<SimiliarNews> similiarnews = newsDao.selectByExample(ex);

        // step-4：热度所属级别
        Integer warnClass = settingService.judgeOpinionSettingClass(maxSeq.getHot());

        // step-4：返回数据
        OpinionInfo rs = BeanMapperUtil.map(maxSeq, OpinionInfo.class);
        rs.setSimiliarNewsList(similiarnews);
        rs.setWarnClass(warnClass);
        return rs;
    }

    /**
     * 解除舆情预警
     * @param uuid
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer removeWarnOpinion(String uuid) {
        OpinionStatus os = new OpinionStatus();
        os.setStatus(2);
        OpinionStatusExample example = new OpinionStatusExample();
        example.createCriteria().andUuidEqualTo(uuid);
        int updateInt = opinionStatusDao.updateByExampleSelective(os, example);
        if(updateInt == 0) throw new ApplicationException(BizErrorCode.OBJECT_NOT_EXIST);
        return updateInt;
    }
}
    
    