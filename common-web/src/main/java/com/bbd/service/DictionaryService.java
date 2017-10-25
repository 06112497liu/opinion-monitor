package com.bbd.service;

import com.bbd.bean.DataDictionaryVO;
import com.bbd.enums.EnumInterface;
import com.bbd.enums.RegionEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tjwang
 * @version $ v 0.1  2017/10/16 15:39 tjwang Exp $
 */
@Service
public class DictionaryService {

    /**
     * 返回不同数据映射
     * @param type
     * @param category
     * @param categoryCode
     * @return
     */
    public List<DataDictionaryVO> listDataDictionaryMap(String type, String category, String categoryCode) {
        List<DataDictionaryVO> data = null;

        if (type.equalsIgnoreCase("region")) {
            //区域
            data = enumToList(RegionEnum.values());
        }

        return data;
    }

    /**
     * 获取区域,行业枚举映射
     * @param enumInterfaces
     * @return
     */
    private List<DataDictionaryVO> enumToList(EnumInterface[] enumInterfaces) {
        List<DataDictionaryVO> list = new ArrayList<>();
        Arrays.asList(enumInterfaces).forEach((e) -> list.add(new DataDictionaryVO(e.getCode(), e.getDesc())));
        return list;
    }

}
