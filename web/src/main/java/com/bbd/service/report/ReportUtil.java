package com.bbd.service.report;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.bbd.report.enums.ParamTypeEnum;
import com.bbd.report.util.ModelUtil;

public class ReportUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ReportUtil.class);
	
	
	public static boolean hasData(Object data) {
		if (data != null && ((List<Object>) data).size() != 0) {
			return true;
		} else {
			return false;
		}
	}
	
    
    public static List<Object> getDataByKey(Object data, String key) {
    	for(Map.Entry map : ((Map<Object, Object>)data).entrySet()){
             String keyTemp = String.valueOf(map.getKey());
             List<Object> listData = (List<Object>) map.getValue();
             if (keyTemp.equals(key)) {//最近一季度或者年
             	return listData;
             }
    	}
    	return new ArrayList<Object>();
    }
    
    public static double getValueByType(List<Object> data, String typeProperty, String valueProperty, String type) {
    	for (Object e : data) {
    		try {
				if (BeanUtils.getProperty(e, typeProperty).equals(type)) {
					return Double.valueOf(BeanUtils.getProperty(e, valueProperty)).doubleValue();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
    	}
    	return 0;
    }
    
    
    public static String returnString(StringBuilder sb) {
    	if (sb.toString().equals("")) {
    		return "";
    	} else {
    		return StringUtils.removeEnd(sb.toString(), "、");
    	}
    }
    
    public static String computeIndustryValue(Object data, String typeProperty, String valueProperty) {
    	List<Object[]> value = new ArrayList<>();
        String[] title = new String[]{"type","num"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc(),ParamTypeEnum.DOUBLE.getDesc()};
    	for(Object e : (List<Object>)data) {
    		try {
    			value.add(new Object[]{BeanUtils.getProperty(e, typeProperty), BeanUtils.getProperty(e, valueProperty)});
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
    	}
    	logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
        return JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
    }
    
    //计算趋势图Trend
    public static String computeTrendValue(Object data, String[] types, String typeProperty, String valueProperty) {
    	List<Object[]> value = new ArrayList<>();
        String[] title = new String[]{"date","type","num"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(),ParamTypeEnum.DOUBLE.getDesc()};
        data = new TreeMap((Map<Object, Object>)data);
        for(Map.Entry map : ((Map<Object, Object>)data).entrySet()){
            String key = String.valueOf(map.getKey());
            List<Object> listData = (List<Object>) map.getValue();
            for (String type : types) {
        		computeTrendValue(key, listData, value, typeProperty, valueProperty, type);
        	}
        }
    	logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
        return JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
    }
    
  //计算趋势图Trend
    public static String computeTrendMultiValue(Object data, String[] types, String typeProperty, String valueProperty1, String valueProperty2) {
    	List<Object[]> value = new ArrayList<>();
        String[] title = new String[]{"date","type1","num1", "type2", "num2"};
        String[] titleType = new String[]{ParamTypeEnum.STRING.getDesc(), ParamTypeEnum.STRING.getDesc(),ParamTypeEnum.DOUBLE.getDesc(), 
        		ParamTypeEnum.STRING.getDesc(),ParamTypeEnum.DOUBLE.getDesc()};
        data = new TreeMap((Map<Object, Object>)data);
        for(Map.Entry map : ((Map<Object, Object>)data).entrySet()){
            String key = String.valueOf(map.getKey());
            List<Object> listData = (List<Object>) map.getValue();
            String lastType = types[types.length - 1];
        	List<Object> objs = new ArrayList<Object>();
        	for(int i = 0; i < types.length - 1; i++){
        		objs.add(key);
        		objs.add(types[i]);
        		objs.add(computeTrendMultiValue(listData, typeProperty, valueProperty1, types[i]));
        	}
        	objs.add(lastType);
        	objs.add(computeTrendMultiValue(listData, typeProperty, valueProperty2, lastType));
        	value.add(objs.toArray());
        }
    	logger.warn(JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(value));
        return JSON.toJSONString(title) + ModelUtil.TAG + JSON.toJSONString(titleType) + ModelUtil.TAG + JSON.toJSONString(value);
    }
    
    //计算趋势图Trend
    public static void computeTrendValue(String key, Object data, List<Object[]> value, String typeProperty, String valueProperty, String type) {
    	if (data == null){
    		//value.add(new Object[]{key, type, 0});
    		return;
    	}
    	for(Object e : (List<Object>)data) {
    		try {
				if (BeanUtils.getProperty(e, typeProperty).equals(type)) {
					value.add(new Object[]{key, type,
							BeanUtils.getProperty(e, valueProperty)
							});
					break;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
    }
    
    //计算趋势图Trend
    public static String computeTrendMultiValue(Object data, String typeProperty, String valueProperty, String type) {
    	if (data == null){
    		return "";
    	}
    	for(Object e : (List<Object>)data) {
    		try {
				if (BeanUtils.getProperty(e, typeProperty).equals(type)) {
					return BeanUtils.getProperty(e, valueProperty);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
		return "";
    }

	// 构建二维数组
	public static <T> Object[][] buildTwoArray(List<T> datas) {

		Integer rows = datas.size();
		if(rows == 0) {
			return null;
		}

		Field[] declaredFields = datas.get(0).getClass().getDeclaredFields();
		Integer columns = declaredFields.length;

		Object[][] rs = new Object[rows][columns];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				declaredFields[j].setAccessible(true);
				try {
					Object obj = declaredFields[j].get(datas.get(i));
					if(obj instanceof String) {
						String str = obj.toString().trim();
						if (str.equals("")) rs[i][j] = "无";
						else rs[i][j] = str;
					}else {
						rs[i][j] = obj;
					}
				} catch (IllegalArgumentException e) {
					logger.error("", e);
				} catch (IllegalAccessException e) {
					logger.error("", e);
				}
			}
		}
		return rs;
	}

}
