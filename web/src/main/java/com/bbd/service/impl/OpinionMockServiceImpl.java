package com.bbd.service.impl;

import com.bbd.domain.WarnSetting;
import com.bbd.service.OpinionService;
import com.bbd.service.vo.WarnOpinionTopTenVo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Liuweibo
 * @version Id: OpinionServiceImpl.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
@Service("opinionMockServiceImpl")
public class OpinionMockServiceImpl implements OpinionService {

    @Override
    public List<WarnOpinionTopTenVo> getWarnOpinionTopTen() {
        List<WarnOpinionTopTenVo> list = Lists.newArrayList();
        for(int i=0; i<10; i++) {
            WarnOpinionTopTenVo v = new WarnOpinionTopTenVo("标题" + (i+1), (int)(Math.random()*3+1), new Date());
            list.add(v);
        }
        return list;
    }
}
    
    