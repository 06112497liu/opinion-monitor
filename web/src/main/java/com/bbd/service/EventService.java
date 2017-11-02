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
import com.bbd.service.vo.GraphVO;
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
	    opinionVO.setEmotion(1);
	    opinionVO.setHot(95);
	    opinionList.add(opinionVO);
	    map.put("items", opinionList);
	    
        return map;
	}
	
	
	/**  
	 * @param id
	 * @param cycle
	 * @param emotion
	 * @return 
	 */
	public  List<KeyValueVO> eventLabelList(Integer id, Integer cycle, Integer emotion){

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
        
        return websiteList;
	}
	
	
	/**  
	 * @param id
	 * @param cycle
	 * @return 
	 */
	public  int eventInfoTotal(Integer id, Integer cycle){
	    return 83732;
	}
	
	
	/**  
	 * @param id
	 * @return 
	 */
	public  int eventHotValue(Integer id){
        return 85;
    }
	
	
	/**  
	 * @param id
	 * @param cycle
	 * @return 
	 */
	public HashMap<String, Object> eventWholeTrend(Integer id, Integer cycle) {
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    List<GraphVO> infoList = new ArrayList<GraphVO>();
	    GraphVO o1 = new GraphVO("2017-09-09 14:00", "信息总量", "12");
	    GraphVO o2 = new GraphVO("2017-09-09 16:00", "信息总量", "18");
	    GraphVO o3 = new GraphVO("2017-09-09 18:00", "信息总量", "12");
        infoList.add(o1);
        infoList.add(o2);
        infoList.add(o3);
	    
        map.put("infoList", infoList);
        
        List<GraphVO> warnList = new ArrayList<GraphVO>();
        GraphVO o4 = new GraphVO("2017-09-09 14:00", "预警总量", "12");
        GraphVO o5 = new GraphVO("2017-09-09 16:00", "预警总量", "18");
        GraphVO o6 = new GraphVO("2017-09-09 18:00", "预警总量", "12");
        warnList.add(o4);
        warnList.add(o5);
        warnList.add(o6);
        
        map.put("warnList", warnList);
       
        return map;
	    
	}
	
	
	/**  
	 * @param id
	 * @param cycle
	 * @return 
	 */
	public HashMap<String, Object> eventSrcDis(Integer id, Integer cycle) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<GraphVO> srcDisList = new ArrayList<GraphVO>();
        GraphVO o1 = new GraphVO(null, "微博", "12");
        GraphVO o2 = new GraphVO(null, "网站", "18");
        GraphVO o3 = new GraphVO(null, "论坛", "12");
        
        srcDisList.add(o1);
        srcDisList.add(o2);
        srcDisList.add(o3);
        map.put("srcDisList", srcDisList);
        return map;
	}
	
	
	/**  
	 * @param id
	 * @param cycle
	 * @return 
	 */
	public HashMap<String, Object> eventInfoTrend(Integer id, Integer cycle) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<GraphVO> allList = new ArrayList<GraphVO>();
        GraphVO o1 = new GraphVO("2017-09-09 14:00", "全部", "12");
        GraphVO o2 = new GraphVO("2017-09-09 16:00", "全部", "18");
        GraphVO o3 = new GraphVO("2017-09-09 18:00", "全部", "12");
        allList.add(o1);
        allList.add(o2);
        allList.add(o3);
        
        map.put("allList", allList);
        
        List<GraphVO> webList = new ArrayList<GraphVO>();
        GraphVO o4 = new GraphVO("2017-09-09 14:00", "网站", "4");
        GraphVO o5 = new GraphVO("2017-09-09 16:00", "网站", "6");
        GraphVO o6 = new GraphVO("2017-09-09 18:00", "网站", "4");
        webList.add(o4);
        webList.add(o5);
        webList.add(o6);
        
        map.put("webList", webList);
        
        List<GraphVO> luntanList = new ArrayList<GraphVO>();
        GraphVO o7 = new GraphVO("2017-09-09 14:00", "论坛", "5");
        GraphVO o8 = new GraphVO("2017-09-09 16:00", "论坛", "7");
        GraphVO o9 = new GraphVO("2017-09-09 18:00", "论坛", "5");
        luntanList.add(o7);
        luntanList.add(o8);
        luntanList.add(o9);
        
        map.put("luntanList", luntanList);
        
        List<GraphVO> weiboList = new ArrayList<GraphVO>();
        GraphVO o10 = new GraphVO("2017-09-09 14:00", "微博", "3");
        GraphVO o11 = new GraphVO("2017-09-09 16:00", "微博", "5");
        GraphVO o12 = new GraphVO("2017-09-09 18:00", "微博", "3");
        weiboList.add(o10);
        weiboList.add(o11);
        weiboList.add(o12);
        
        map.put("weiboList", weiboList);
        
        return map;
    }
	
	
	/**  
	 * @param id
	 * @param cycle
	 * @return 
	 */
	public HashMap<String, Object> eventSrcActive(Integer id, Integer cycle) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<GraphVO> srcActiveList = new ArrayList<GraphVO>();
        GraphVO o1 = new GraphVO(null, "新浪微博", "12");
        GraphVO o2 = new GraphVO(null, "百度贴吧", "18");
        GraphVO o3 = new GraphVO(null, "贵阳网", "12");
        
        srcActiveList.add(o1);
        srcActiveList.add(o2);
        srcActiveList.add(o3);
        map.put("srcActiveList", srcActiveList);
        return map;
    }
	
	public HashMap<String, Object> eventTrend(Integer id, Integer cycle, Integer pageNo, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        List<OpinionVO> trendList = new ArrayList<OpinionVO>();
        OpinionVO o1 = new OpinionVO();
        o1.setTitle("京昆高速多车相撞，4死5伤");
        o1.setUuid("001");
        o1.setWebsite("新华网");
        o1.setSimiliarCount(45);
        trendList.add(o1);
        
        OpinionVO o2 = new OpinionVO();
        o2.setTitle("京昆高速多车相撞，5死4伤");
        o2.setUuid("002");
        o2.setWebsite("贵阳网");
        o2.setSimiliarCount(49);
        trendList.add(o2);
        
        OpinionVO o3 = new OpinionVO();
        o3.setTitle("京昆高速多车相撞，6死3伤");
        o3.setUuid("003");
        o3.setWebsite("新浪网");
        o3.setSimiliarCount(58);
        trendList.add(o3);
        
        trendList.add(o3);
        map.put("trendList", trendList);
        map.put("eventTime", "2017-01-02");
        return map;
    }
	
	public HashMap<String, Object> eventKeywords(Integer id, Integer cycle) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<GraphVO> eventKeywords = new ArrayList<GraphVO>();
        GraphVO o1 = new GraphVO(null, "天价", "999");
        GraphVO o2 = new GraphVO(null, "诈骗", "700");
        GraphVO o3 = new GraphVO(null, "宰客", "600");
        
        GraphVO o4 = new GraphVO(null, "强制消费", "500");
        GraphVO o5 = new GraphVO(null, "欺诈", "400");
        GraphVO o6 = new GraphVO(null, "虚假", "300");
        
        GraphVO o7 = new GraphVO(null, "不合格", "250");
        GraphVO o8 = new GraphVO(null, "投诉", "200");
        GraphVO o9 = new GraphVO(null, "超标", "100");
        
        GraphVO o10 = new GraphVO(null, "瓜娃子", "20");
        
        eventKeywords.add(o1);
        eventKeywords.add(o2);
        eventKeywords.add(o3);
        
        eventKeywords.add(o4);
        eventKeywords.add(o5);
        eventKeywords.add(o6);
        
        eventKeywords.add(o7);
        eventKeywords.add(o8);
        eventKeywords.add(o9);
        
        eventKeywords.add(o10);
        
        map.put("eventKeywords", eventKeywords);
        return map;
    }
    
	public HashMap<String, Object> eventDataType(Integer id, Integer cycle) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<GraphVO> eventDataTypeList = new ArrayList<GraphVO>();
        GraphVO o1 = new GraphVO(null, "敏感", "80");
        GraphVO o2 = new GraphVO(null, "非敏感", "20");
        eventDataTypeList.add(o1);
        eventDataTypeList.add(o2);
        map.put("eventKeywords", eventDataTypeList);
        return map;
    }  
	
}
