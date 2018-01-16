package com.bbd.service.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bbd.bean.Reptile;
import com.bbd.domain.KeyValueVO;
import com.bbd.util.HttpUtil;
import com.bbd.util.RemoteConfigUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 业务工具
 * @author Liuweibo
 * @version Id: BusinessUtils.java, v0.1 2017/11/9 Liuweibo Exp $$
 */
public class BusinessUtils {

    /**
     * 根据时间跨度获取时间
     * @param timeSpan 1.近24小时 2.近7天 3.近30天
     * @return
     */
    public static DateTime getDateByTimeSpan(Integer timeSpan) {
        DateTime now = DateTime.now();
        DateTime startTime = null;
        if(2 == timeSpan) startTime = now.plusDays(-7).withTimeAtStartOfDay();
        else if(3 == timeSpan) startTime = now.plusDays(-30).withTimeAtStartOfDay();
        else if(1 == timeSpan)startTime = now.plusHours(-24);
        return startTime;
    }

    public static DateTime getDateTimeWithStartTime(Integer timeSpan) {
        DateTime now = DateTime.now();
        DateTime result;
        switch (timeSpan) {
            case 1:
                result = now.withTimeAtStartOfDay();
                break;
            case 2:
                result = now.withDayOfWeek(1).withTimeAtStartOfDay();
                break;
            case 3:
                result = now.withDayOfMonth(1).withTimeAtStartOfDay();
                break;
            case 4:
                result = now.withDayOfYear(1).withTimeAtStartOfDay();
                break;
            default:
                result = now.plusYears(-8);
                break;
        }
        return result;
    }

    public static DateHistogramInterval getDateHistogramInterval(Integer timeSpan) {
        DateHistogramInterval d = null;
        switch (timeSpan) {
            case 1:
                d = DateHistogramInterval.HOUR;
                break;
            case 2:
            case 3:
                d = DateHistogramInterval.DAY;
                break;
            case 4:
                d = DateHistogramInterval.MONTH;
                break;
            default:
                d = DateHistogramInterval.YEAR;
                break;
        }
        return d;
    }

    // 根据keys构建KeyValueVO
    public static List<KeyValueVO> buildAllVos(Set<String> keys, Set<String> allKeys) {
        Set<String> noContain = Sets.newHashSet();
        for (String str : allKeys) {
            if (!keys.contains(str)) noContain.add(str);
        }
        List<KeyValueVO> rs = Lists.newLinkedList();
        for (String key : noContain) {
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(key);
            vo.setName(key);
            vo.setValue(0);
            rs.add(vo);
        }
        return rs;
    }


    // 解析舆情content字段
    public static String buildContent(List<String> list) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String str : list) {
            String trimStr = str.trim();
            if (trimStr.startsWith("pic_rowkey")) {
                String imgUrl = getPicParse(trimStr);
                if (imgUrl != null) {
                    sb.append("<img src='" + imgUrl + "'>");
                }
            } else if(trimStr.isEmpty()) {
                sb.append("<br/>");
            } else {
                if (i == 0) sb.append(trimStr);
                else sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + trimStr);
            }
            i++;
        }
        return sb.toString();
    }

    // 调用bbd数据平台接口，获取图片地址
    private static String getPicParse(String picCode) {

        String url = RemoteConfigUtil.get(Reptile.PIC_PARSE_URL);
        String ak = RemoteConfigUtil.get(Reptile.PIC_PARSE_AK);
        String path = RemoteConfigUtil.get(Reptile.PIC_PARSE_PATH);
        String full_url = url + path;
        Map<String, String> params = Maps.newHashMap();
        params.put("key", picCode);
        params.put("appkey", ak);
        String resp = HttpUtil.getHttp(full_url, params);

        JSONObject obj = JSONObject.parseObject(resp);
        JSONArray imgResp = obj.getJSONArray("results");
        String imgUrl = null;
        for (int i=0; i<imgResp.size(); i++) {
            JSONObject o = imgResp.getJSONObject(i);
            if(o.containsKey("img_url")) {
                imgUrl = o.getString("img_url");
            }
        }
        return imgUrl;
    }

}
    
    