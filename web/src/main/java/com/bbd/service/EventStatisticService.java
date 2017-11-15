

/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
 package com.bbd.service; 

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.bbd.bean.OpinionEsVO;
import com.bbd.dao.OpinionEventDao;
import com.bbd.dao.OpinionEventMediaStatisticDao;
import com.bbd.dao.OpinionEventTrendStatisticDao;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventExample;
import com.bbd.domain.OpinionEventMediaStatistic;
import com.bbd.domain.OpinionEventTrendStatistic;
import com.bbd.domain.OpinionEventTrendStatisticExample;
import com.bbd.service.vo.KeyValueVO;
import com.mybatis.domain.PageBounds;

/** 
 * @author daijinlong 
 * @version $Id: EventStatisticService.java, v 0.1 2017年11月14日 上午10:23:47 daijinlong Exp $ 
 */
public class EventStatisticService {
    @Autowired
    OpinionEventDao opinionEventDao;
    @Autowired
    private EsQueryService esQueryService;
    @Autowired
    private EventService eventService;
    @Autowired
    OpinionEventTrendStatisticDao opinionEventTrendStatisticDao;
    @Autowired
    OpinionEventMediaStatisticDao opinionEventMediaStatisticDao;
    
    public void eventTrendStatistic() {
        OpinionEventExample example = new OpinionEventExample();
        example.createCriteria().andIsDeleteEqualTo((byte)0).andFileReasonIsNull();
        List<OpinionEvent> opinionEvents = opinionEventDao.selectByExample(example);
        
        List<OpinionEventTrendStatistic>  records = new ArrayList<OpinionEventTrendStatistic>();
        for (OpinionEvent e : opinionEvents) {
            OpinionEsVO vo = esQueryService.queryEventMaxOpinion(e.getId());
            OpinionEventTrendStatisticExample expl = new OpinionEventTrendStatisticExample();
            expl.setOrderByClause("gmt_create DESC");
            expl.createCriteria().andEventIdEqualTo(e.getId());
            List<OpinionEventTrendStatistic> eventTrendStaList = opinionEventTrendStatisticDao.selectByExampleWithPageBounds(expl, new PageBounds(1,1));
            if (eventTrendStaList != null && eventTrendStaList.size() > 0 
                    && eventTrendStaList.get(0).getUuid().equals(vo.getUuid())) {
                
            } else {
                OpinionEventTrendStatistic evtTrend = new OpinionEventTrendStatistic();
                evtTrend.setEventId(e.getId());
                BeanUtils.copyProperties(vo, evtTrend);
                evtTrend.setKeys(StringUtils.join(vo.getKeys(), ","));
                evtTrend.setKeyword(StringUtils.join(vo.getKeyword(), ","));
                evtTrend.setGmtCreate(new Date());
                records.add(evtTrend);
            }
        }
        opinionEventTrendStatisticDao.insertBatch(records);
    }
    
    
    public void eventMediaStatistic() throws ParseException {
        OpinionEventExample example = new OpinionEventExample();
        example.createCriteria().andIsDeleteEqualTo((byte)0).andFileReasonIsNull();
        List<OpinionEvent> opinionEvents = opinionEventDao.selectByExample(example);
        List<OpinionDictionary> opinionDictionaryList = eventService.getDictionary("F");
        List<OpinionEventMediaStatistic> records = new ArrayList<OpinionEventMediaStatistic>();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTime now = DateTime.now();
        String pickTime = now.toString("yyyy-MM-dd HH") + ":00:00";
        Date date = format.parse(pickTime);
        for (OpinionEvent e : opinionEvents) {
            int total = 0;
            OpinionEventMediaStatistic all = new OpinionEventMediaStatistic();
            for (OpinionDictionary f : opinionDictionaryList) {
                KeyValueVO tmp = esQueryService.getEventMediaStatisticBySource(e.getId(), f.getCode());
                OpinionEventMediaStatistic evtMedia = new OpinionEventMediaStatistic();
                evtMedia.setEventId(e.getId());
                evtMedia.setMediaCode(f.getCode());
                evtMedia.setMediaCount(Integer.valueOf(tmp.getValue().toString()));
                evtMedia.setGmtCreate(new Date());
                evtMedia.setPickTime(date);
                records.add(evtMedia);
                total = total + evtMedia.getMediaCount();
                BeanUtils.copyProperties(evtMedia, all);
            }
            all.setMediaCount(total);
            all.setMediaCode("all");
            records.add(all);
            total = 0;
        }
        opinionEventMediaStatisticDao.insertBatch(records);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}

