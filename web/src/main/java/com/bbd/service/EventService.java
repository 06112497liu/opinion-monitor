package com.bbd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbd.dao.OpinionDictionaryDao;
import com.bbd.dao.OpinionEventDao;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionDictionaryExample;
import com.bbd.domain.OpinionEvent;
import com.bbd.service.EventService;


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
		opinionEventDao.updateByPrimaryKey(opinionEvent);
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
