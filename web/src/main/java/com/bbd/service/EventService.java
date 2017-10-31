package com.bbd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbd.dao.OpinionDictionaryDao;
import com.bbd.dao.OpinionEventDao;
import com.bbd.dao.WarnSettingDao;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionDictionaryExample;
import com.bbd.domain.OpinionEvent;
import com.bbd.domain.OpinionEventExample;
import com.bbd.domain.OpinionEventExample.Criteria;
import com.bbd.domain.WarnSetting;
import com.bbd.service.vo.KeyValueVO;
import com.bbd.service.vo.OpinionVO;
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
	@Autowired
	WarnSettingDao warnSettingDao;
	/**  
	 * @param opinionEvent 
	 */
	public void createEvent(OpinionEvent opinionEvent) {
	    opinionEvent.setIsDelete((byte)0);
		opinionEventDao.insert(opinionEvent);
		
		WarnSetting recordNew = new WarnSetting();
		recordNew.setEventId(opinionEvent.getId());
		recordNew.setType(1);
		recordNew.setTargetType(2);
		recordNew.setPopup(1);
		recordNew.setLevel(1);
		recordNew.setMin(60);
		recordNew.setMax(100);
		recordNew.setName("事件新增观点预警");
		recordNew.setCreateBy(opinionEvent.getCreateBy());
		warnSettingDao.insert(recordNew);
		
		WarnSetting recordWhole1 = new WarnSetting();
		recordWhole1.setEventId(opinionEvent.getId());
		recordWhole1.setType(2);
		recordWhole1.setTargetType(2);
		recordWhole1.setPopup(1);
		recordWhole1.setLevel(1);
		recordWhole1.setMin(80);
		recordWhole1.setMax(100);
		recordWhole1.setName("事件总体热度预警");
		recordWhole1.setCreateBy(opinionEvent.getCreateBy());
        warnSettingDao.insert(recordWhole1);
        
        WarnSetting recordWhole2 = new WarnSetting();
        recordWhole2.setEventId(opinionEvent.getId());
        recordWhole2.setType(2);
        recordWhole2.setTargetType(2);
        recordWhole2.setPopup(1);
        recordWhole2.setLevel(2);
        recordWhole2.setMin(60);
        recordWhole2.setMax(79);
        recordWhole2.setName("事件总体热度预警");
        recordWhole2.setCreateBy(opinionEvent.getCreateBy());
        warnSettingDao.insert(recordWhole2);
        
        WarnSetting recordWhole3 = new WarnSetting();
        recordWhole3.setEventId(opinionEvent.getId());
        recordWhole3.setType(2);
        recordWhole3.setTargetType(2);
        recordWhole3.setPopup(1);
        recordWhole3.setLevel(3);
        recordWhole3.setMin(40);
        recordWhole3.setMax(59);
        recordWhole3.setName("事件总体热度预警");
        recordWhole3.setCreateBy(opinionEvent.getCreateBy());
        warnSettingDao.insert(recordWhole3);
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
     * @param pageNo
     * @param pageSize
     * @return 
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
        criteria.andIsDeleteEqualTo((byte)0)
                .andFileReasonIsNull();
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
	
	
	/**  
	 * @param id
	 * @param cycle
	 * @param emotion
	 * @param source
	 * @param pageNo
	 * @param pageSize
	 * @return 
	 */
	public  HashMap<String, Object> getEventInfoList(Integer id, Integer cycle, Integer emotion, String source, Integer pageNo, Integer pageSize) {
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put("infoTotalNum", 86754);
	    map.put("eventHotVal", 86);
	    map.put("pageNo", 1);
	    map.put("pageSize", 10);
	    map.put("total", 20);
	    List<OpinionVO> opinionList = new ArrayList<OpinionVO>();
	    OpinionVO opinionVO = new OpinionVO();
	    opinionVO.setUuid("111");
	    opinionVO.setTitle("AAA");
	    opinionVO.setSummary("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
	    opinionVO.setWebsite("贵阳网");
	    opinionVO.setLevel(1);
	    opinionVO.setStartTime("2017-05-09 11:45");
	    opinionVO.setEmotion(1);
	    opinionVO.setHot(95);
	    opinionList.add(opinionVO);
	    map.put("items", opinionList);
	    
	    List<KeyValueVO> websiteList = new ArrayList<KeyValueVO>();
	    KeyValueVO websiteNew = new KeyValueVO();
	    websiteNew.setKey("all");
	    websiteNew.setValue(20);
	    websiteNew.setName("全部");
	    websiteList.add(websiteNew);
	    
	    KeyValueVO websiteWeb = new KeyValueVO();
	    websiteWeb.setKey("002");
	    websiteWeb.setValue(2);
	    websiteWeb.setName("网站");
        websiteList.add(websiteWeb);
        
        KeyValueVO websiteWeChat = new KeyValueVO();
        websiteWeChat.setKey("003");
        websiteWeChat.setValue(2);
        websiteWeChat.setName("微信");
        websiteList.add(websiteWeChat);
        
        KeyValueVO websiteLuntan = new KeyValueVO();
        websiteLuntan.setKey("004");
        websiteLuntan.setValue(2);
        websiteLuntan.setName("论坛");
        websiteList.add(websiteLuntan);
        
        KeyValueVO websiteWeibo = new KeyValueVO();
        websiteWeibo.setKey("005");
        websiteWeibo.setValue(10);
        websiteWeibo.setName("微博");
        websiteList.add(websiteWeibo);
        
        KeyValueVO websiteParty = new KeyValueVO();
        websiteParty.setKey("006");
        websiteParty.setValue(10);
        websiteParty.setName("政务");
        websiteList.add(websiteParty);
        
        KeyValueVO websiteOther = new KeyValueVO();
        websiteOther.setKey("007");
        websiteOther.setValue(10);
        websiteOther.setName("其他");
        websiteList.add(websiteOther);
        
        map.put("labels", websiteList);
        return map;
	}
	
	HashMap<String, Object> eventWholeTrend(Integer id, Integer cycle) {
	    HashMap<String, Object> map = new HashMap<String, Object>();
	   /* List<Object>
        map.put("infoTotalNum", 86754);
        map.put("eventHotVal", 86);
        map.put("pageNo", 1);
        map.put("pageSize", 10);
        map.put("total", 20);*/
        return null;
	    
	}
	
}
