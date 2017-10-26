package com.bbd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbd.dao.OpinionDictionaryDao;
import com.bbd.dao.OpinionEventDao;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionDictionaryExample;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventExample;
import com.bbd.domain.OpinionEventExample.Criteria;
import com.bbd.service.EventService;
import com.mybatis.domain.PageBounds;


/** 
 * @author daijinlong 
 * @version $Id: EventService.java, v 0.1 2017年10月25日 下午2:06:26 daijinlong Exp $ 
 */
@Service
public class EventService{

	@Autowired
	OpinionEventDao opinionEventDao;
	@Autowired
	OpinionDictionaryDao opinionDictionaryDao;
	
	/**  
	 * @param opinionEvent 
	 */
	public void createEvent(OpinionEvent opinionEvent) {
	    opinionEvent.setIsDelete((byte)0);
		opinionEventDao.insert(opinionEvent);
	}
	
	
	/**  
	 * @param id
	 * @return 
	 */
	public OpinionEvent getEvent(long id) {
		return opinionEventDao.selectByPrimaryKey(id);
	}

	
	/**  
	 * @param opinionEvent 
	 */
	public void modifyEvent(OpinionEvent opinionEvent) {
		opinionEventDao.updateByPrimaryKeySelective(opinionEvent);
	}
	
	/**  
     * @param opinionEvent 
     */
    public void deleteEvent(OpinionEvent opinionEvent) {
        opinionEvent.setIsDelete((byte)1);
        opinionEventDao.updateByPrimaryKeySelective(opinionEvent);
    }
    
    /**  
     * @param opinionEvent 
     */
    public List<OpinionEvent> eventList(OpinionEvent opinionEvent, Integer pageNo, Integer pageSize) {
        PageBounds pageBounds = new PageBounds(pageNo, pageSize);
        OpinionEventExample  example = new OpinionEventExample();
        example.setOrderByClause("gmt_create DESC");
        Criteria criteria = example.createCriteria();
        if (opinionEvent.getRegion() != null) {
            criteria.andRegionEqualTo(opinionEvent.getRegion());
        }
        if (opinionEvent.getEventGroup() != null) {
            criteria.andEventGroupEqualTo(opinionEvent.getEventGroup());
        }
        return opinionEventDao.selectByExampleWithPageBounds(example, pageBounds);
    }
	
	
	/**  
	 * @param parent
	 * @return 
	 */
	public List<OpinionDictionary> getDictionary(String parent) {
	    OpinionDictionaryExample example = new OpinionDictionaryExample();
	    example.createCriteria().andParentEqualTo(parent);
	    return opinionDictionaryDao.selectByExample(example);
    }

}
