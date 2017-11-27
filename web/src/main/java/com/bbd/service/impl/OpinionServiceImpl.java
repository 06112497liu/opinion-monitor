package com.bbd.service.impl;

import com.bbd.annotation.TimeUsed;
import com.bbd.bean.OpinionEsVO;
import com.bbd.bean.OpinionHotEsVO;
import com.bbd.bean.WarnNotifierVO;
import com.bbd.constant.EsConstant;
import com.bbd.dao.WarnNotifierExtDao;
import com.bbd.domain.WarnSetting;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.job.vo.Content;
import com.bbd.job.vo.MsgVO;
import com.bbd.job.vo.OpinionMsgModel;
import com.bbd.service.EsQueryService;
import com.bbd.service.EventService;
import com.bbd.service.OpinionService;
import com.bbd.service.SystemSettingService;
import com.bbd.service.utils.BusinessUtils;
import com.bbd.service.vo.*;
import com.bbd.util.*;
import com.bbd.vo.UserInfo;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import com.mybatis.domain.Paginator;
import com.mybatis.util.PageListHelper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Liuweibo
 * @version Id: OpinionServiceImpl.java, v0.1 2017/10/31 Liuweibo Exp $$
 */
@Service
public class OpinionServiceImpl implements OpinionService {

    @Autowired
    private EsQueryService esQueryService;

    @Autowired
    private SystemSettingService systemSettingService;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private WarnNotifierExtDao notifierExtDao;

    @Autowired
    private EventService eventService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<WarnOpinionTopTenVO> getWarnOpinionTopTen() {
        List<WarnOpinionTopTenVO> result = Lists.newLinkedList();
        List<OpinionEsVO> esList = esQueryService.getWarnOpinionTopTen();
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3); // 预警配置
        esList.forEach(o -> {
            WarnOpinionTopTenVO v = new WarnOpinionTopTenVO();
            v.setTime(o.getPublishTime());
            v.setHot(o.getHot());
            v.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot(), setting));
            v.setTitle(o.getTitle());
            result.add(v);
        });
        result.sort((x1, x2) -> ComparisonChain.start().compare(x1.getLevel(), x2.getLevel()).compare(x2.getHot(), x1.getHot()).result());
        return result;
    }

    /**
     * 预警舆情列表
     * @param timeSpan
     * @param emotion
     * @param sourceType
     * @return
     */
    @Override
    @TimeUsed
    public Map<String, Object> getWarnOpinionList(Integer timeSpan, Integer emotion, Integer sourceType, PageBounds pb) {

        // step-1：查询es
        OpinionEsSearchVO esResult = esQueryService.queryWarningOpinion(BusinessUtils.getDateByTimeSpan(timeSpan), emotion, sourceType, pb);
        List<OpinionVO> opinions = BeanMapperUtil.mapList(esResult.getOpinions(), OpinionVO.class);
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3); // 预警配置
        opinions.forEach(o -> {
            o.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot(), setting));
        });

        // step-2：分页并返回结果
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), esResult.getTotal().intValue());
        PageList p = PageListHelper.create(opinions, paginator);
        Map<String, Object> map = Maps.newHashMap();
        map.put("opinionsList", p);
        List<KeyValueVO> mediaTypeSta = esResult.getMediaTypeStats();
        List<KeyValueVO> fullMediaTypeSta = eventService.calAllMedia(mediaTypeSta);
        map.put("mediaTypeCount", fullMediaTypeSta);
        map.put("levelCount", esResult.getHotLevelStats());

        return map;
    }

    /**
     * 热点舆情top100
     * @param timeSpan
     * @param emotion
     * @param pb
     * @return
     */
    @Override
    @TimeUsed
    public PageList<OpinionVO> getHotOpinionListTop100(Integer timeSpan, Integer emotion, PageBounds pb) {

        // step-1：查询es
        OpinionEsSearchVO esResult = esQueryService.queryTop100HotOpinion(BusinessUtils.getDateByTimeSpan(timeSpan), emotion);
        List<OpinionEsVO> esOpinons = esResult.getOpinions();

        // step-2：代码分页
        List<OpinionVO> allOpinions = BeanMapperUtil.mapList(esOpinons, OpinionVO.class);

        int firstIndex = pb.getOffset(); int toIndex = pb.getLimit() * pb.getPage();
        if(toIndex > allOpinions.size()) {
            toIndex = allOpinions.size();
        }
        if(firstIndex > toIndex) {
            firstIndex = 0;
            pb.setPage(1);
        }
        List<OpinionVO> opinions = allOpinions.subList(firstIndex, toIndex);
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3); // 预警配置
        opinions.forEach(o -> {
            o.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot(), setting));
        });
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), allOpinions.size());
        PageList p = PageListHelper.create(opinions, paginator);

        return p;
    }

    /**
     * 热点舆情模糊查询
     * @param keyword
     * @return
     */
    @Override
    public PageList<OpinionVO> getHotOpinionList(String keyword, Integer timeSpan, Integer emotion, PageBounds pb) {
        // 保存关键词
        saveKeyword(keyword);

        DateTime startTime = BusinessUtils.getDateByTimeSpan(timeSpan);

        OpinionEsSearchVO esResult = esQueryService.getHotOpinionList(keyword, startTime, emotion, pb);
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), esResult.getTotal().intValue());
        List<OpinionVO> list = BeanMapperUtil.mapList(esResult.getOpinions(), OpinionVO.class);
        PageList<OpinionVO> result = PageListHelper.create(list, paginator);
        return result;
    }

    @Override
    public Map<String, Object> getHistoryWarnOpinionList(Date startTime, Date endTime, Integer emotion, Integer mediaType, PageBounds pb) {
        DateTime start = new DateTime(startTime);
        DateTime endTemp = new DateTime(endTime);
        int year = endTemp.getYear(); int month = endTemp.getMonthOfYear(); int day = endTemp.getDayOfMonth();
        DateTime end = new DateTime(year, month, day, 23, 59, 59);

        // step-1：查询es
        OpinionEsSearchVO esResult = esQueryService.queryHistoryOpinions(start, end, emotion, mediaType, pb);
        List<OpinionVO> opinions = BeanMapperUtil.mapList(esResult.getOpinions(), OpinionVO.class);
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3); // 预警配置
        opinions.forEach(o -> {
            o.setLevel(systemSettingService.judgeOpinionSettingClass(o.getHot(), setting));
        });

        // step-2：分页并返回结果
        Paginator paginator = new Paginator(pb.getPage(), pb.getLimit(), esResult.getTotal().intValue());
        PageList p = PageListHelper.create(opinions, paginator);
        Map<String, Object> map = Maps.newHashMap();
        map.put("opinionsList", p);
            // 媒体类型中文描述转化
        List<KeyValueVO> mediaTypeList = esResult.getMediaTypeStats();
        List<KeyValueVO> fullMediaTypeList = eventService.calAllMedia(mediaTypeList);
        map.put("mediaTypeCount", fullMediaTypeList);
        map.put("levelCount", esResult.getHotLevelStats());

        return map;
    }

    /**
     * 舆情详情
     * @param uuid
     * @return
     */
    @Override
    public OpinionExtVO getOpinionDetail(String uuid) {
        OpinionEsVO o = esQueryService.getOpinionByUUID(uuid);
        OpinionExtVO result = BeanMapperUtil.map(o, OpinionExtVO.class);
        // 判断预警级别
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3); // 预警配置
        Integer level = systemSettingService.judgeOpinionSettingClass(result.getHot(), setting);
        result.setLevel(level);
        return result;
    }

    /**
     * 历史关键词搜索查询
     * @return
     */
    @Override
    public List<String> getHistoryWordSearch() {
        ListOperations listOperation = redisTemplate.opsForList();
        UserInfo user = UserContext.getUser();
        if(Objects.isNull(user)) throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "未登录");
        List list = listOperation.range("com.bbd.service.impl.OpinionServiceImpl.getHistoryWordSearch->" + UserContext.getUser().getUsername(), 0, 9);
        return list;
    }

    // 保存历史搜索关键词
    private void saveKeyword(String keyword) {
        ListOperations listOperation = redisTemplate.opsForList();
        UserInfo user = UserContext.getUser();
        if(Objects.isNull(user)) throw new ApplicationException(CommonErrorCode.BIZ_ERROR, "未登录");
        List list = listOperation.range("com.bbd.service.impl.OpinionServiceImpl.getHistoryWordSearch->" + UserContext.getUser().getUsername(), 0, 9);
        if(!StringUtils.isEmpty(keyword) && !list.contains(keyword))
            listOperation.leftPush("com.bbd.service.impl.OpinionServiceImpl.getHistoryWordSearch->" + UserContext.getUser().getUsername(), keyword);
    }

    @Override
    public List<SimiliarNewsVO> getOpinionSimiliarNewsList(String uuid, PageBounds pb) {
        List<SimiliarNewsVO> result = esQueryService.querySimiliarNews(uuid, pb);
        return result;
    }

    /**
     * 获取舆情热度走势
     * @param uuid
     * @param timeSpan
     * @return
     */
    @Override
    public List<KeyValueVO> getOpinionHotTrend(String uuid, Integer timeSpan) {
        DateTime startTime = BusinessUtils.getDateByTimeSpan(timeSpan);
        List<OpinionHotEsVO> result = esQueryService.getOpinionHotTrend(uuid, startTime);

        // 根据横坐标去重
        Set<OpinionHotEsVO> set;
        if(timeSpan == 1)
            set = new TreeSet<>(Comparator.comparing(o -> new DateTime(o.getHotTime()).toString("yyyy-MM-dd HH:00:00")));
        else
            set = new TreeSet<>(Comparator.comparing(o -> new DateTime(o.getHotTime()).toString("yyyy-MM-dd")));

        set.addAll(result);
        List<OpinionHotEsVO> list = new ArrayList<>();
        list.addAll(set);
        list.sort((o1, o2) -> -(o1.getHotTime().compareTo(o2.getHotTime())));
        List<KeyValueVO> keyValueVOList = Lists.newLinkedList();
        for (OpinionHotEsVO v : list) {
            KeyValueVO vo = new KeyValueVO();
            String time = DateUtil.formatDateByPatten(v.getHotTime(), "yyyy-MM-dd HH:mm");
            vo.setKey(time);
            vo.setName(time);
            vo.setValue(v.getHot());
            keyValueVOList.add(vo);
        }
        return keyValueVOList;
    }

    /**
     * 获取短信或邮件提醒的json字符串
     * @param lastSendTime
     */
    @Override
    public OpinionMsgSend getWarnRemindJson(DateTime lastSendTime) {

        List<MsgVO> result = Lists.newLinkedList();
        Date date = new Date();
        OpinionMsgSend msgSend = new OpinionMsgSend();

        // step-1：获取该时间段内分级预警增加量，一级每个预警的热度最大值，一级预警通知人
        Integer oneMax = esQueryService.queryMaxHot(lastSendTime, 1);
        Integer twoMax = esQueryService.queryMaxHot(lastSendTime, 2);
        Integer threeMax = esQueryService.queryMaxHot(lastSendTime, 3);
        Map<Integer, Integer> maxMap = Maps.newHashMap();
        maxMap.put(1, oneMax); maxMap.put(2, twoMax); maxMap.put(1, threeMax);
        Map<Integer, Integer> mapAdd = esQueryService.queryAddWarnCount(lastSendTime);
        List<WarnNotifierVO> notifies = notifierExtDao.queryNotifierList(3);

        // step-2：邮件发送
        Map<String, List<WarnNotifierVO>> emailNotifier = notifies.stream()
                .filter(p -> p.getEmailNotify() == 1) // 过滤出需要通过邮件发送的
                .collect(Collectors.groupingBy(WarnNotifierVO::getEmail)); // 以邮箱分组
        List<MsgVO> emailMsg = buidMsgVO("email", maxMap, mapAdd, emailNotifier);
        // step-2：短信发送
        Map<String, List<WarnNotifierVO>> smsNotifier = notifies.stream()
                .filter(p -> p.getSmsNotify() == 1) // 过滤出需要通过短信发送的
                .collect(Collectors.groupingBy(WarnNotifierVO::getPhone)); // 以电话分组
        List<MsgVO> smsMsg = buidMsgVO("sms", maxMap, mapAdd, smsNotifier);
        result.addAll(emailMsg);
        result.addAll(smsMsg);
        msgSend.setSendMsg(result);
        msgSend.setClaTime(date);
        return msgSend;
    }

    List<MsgVO> buidMsgVO(String type,
                          Map<Integer, Integer> maxMap,
                          Map<Integer, Integer> mapAdd,
                          Map<String, List<WarnNotifierVO>> notifies) {
        List<MsgVO> result = Lists.newLinkedList();
        for (String k : notifies.keySet()) {
            MsgVO msgVO = new MsgVO();
            Content content = new Content();
            OpinionMsgModel model = new OpinionMsgModel();
            content.setSubject("分级舆情预警");
            content.setSubject("classify_opinion_warnning");
            content.setRetry(3);
            content.setTo(k);
            content.setModel(model);
            List<WarnNotifierVO> list = notifies.get(k);
            Integer max = 3;
            for (WarnNotifierVO p : list) {
                Integer level = p.getLevel();
                Integer value = mapAdd.get(level);
                if(level < max)
                    max = level;
                if(level == 1)
                    model.setLevelOne(value);
                else if(level == 2)
                    model.setLevelTwo(value);
                else if(level == 3)
                    model.setLevelThree(value);
                model.setUsername(p.getNotifier());
            }
            model.setScore(maxMap.get(max).toString());
            msgVO.setType(type);
            msgVO.setContent(content);
            result.add(msgVO);
        }
        return result;
    }

    /**
     * 历史预警舆情详情
     * @param uuid
     */
    @Override
    public HistoryOpinionDetailVO getHistoryWarnOpinionDetail(String uuid) {
        // step-1：舆情详情
        OpinionEsVO esVO = esQueryService.queryHistoryWarnDetail(uuid);
        HistoryOpinionDetailVO rs = BeanMapperUtil.map(esVO, HistoryOpinionDetailVO.class);
        rs.setFirstWarnTime(esVO.getWarnTime().getFirstWarnTime());

        // step-2：操作记录
        Map<String, Object> keyMap = Maps.newHashMap();
        keyMap.put(EsConstant.uuidField, uuid);
        List<OpinionOpRecordVO> records = esQueryService.getOpinionOpRecordByUUID(keyMap, 1000);
        rs.setRecords(records);

        // step-3：舆情等级
        List<WarnSetting> setting = systemSettingService.queryWarnSetting(3);
        rs.setLevel(systemSettingService.judgeOpinionSettingClass(rs.getHot(), setting));

        return rs;
    }
}












    
    