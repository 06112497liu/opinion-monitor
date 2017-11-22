

/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
 package com.bbd.bean; 

import java.util.Date;

/** 
 * @author daijinlong 
 * @version $Id: EventEsVO.java, v 0.1 2017年11月22日 上午10:48:13 daijinlong Exp $ 
 */
public class EventEsVO {
    
    private Long eventId;
    
    private String opinionId;
    
    private Date matchTime;
    
    private Date matchTimeTrim;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getOpinionId() {
        return opinionId;
    }

    public void setOpinionId(String opinionId) {
        this.opinionId = opinionId;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public Date getMatchTimeTrim() {
        return matchTimeTrim;
    }

    public void setMatchTimeTrim(Date matchTimeTrim) {
        this.matchTimeTrim = matchTimeTrim;
    }
    
}

