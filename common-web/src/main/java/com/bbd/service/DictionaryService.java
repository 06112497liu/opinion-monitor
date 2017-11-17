package com.bbd.service;

import com.bbd.bean.DataDictionaryVO;
import com.bbd.dao.OpinionDictionaryDao;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionDictionaryExample;
import com.bbd.enums.EnumInterface;
import com.bbd.enums.RegionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tjwang
 * @version $ v 0.1  2017/10/16 15:39 tjwang Exp $
 */
@Service
public class DictionaryService {

    @Autowired
    private OpinionDictionaryDao dictionaryDao;

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

    /**
     * 获取字典下拉列表
     * @param type A-(奶粉、食品等项里列表); B-(工商局，其他列表查询); C-(区域查询); D-(级别查询); E-(时间状态查询); F-(媒体类型查询); G-(转发内容查询); H-(情感查询); I-(工商局列表)
     * @return
     */
    public Map<String, String> queryDictionary(String type) {
        OpinionDictionaryExample example = new OpinionDictionaryExample();
        example.createCriteria().andParentEqualTo(type);
        List<OpinionDictionary> list = dictionaryDao.selectByExample(example);
        Map<String, String> map = list.stream().collect(Collectors.toMap(OpinionDictionary::getCode, OpinionDictionary::getName));
        return map;
    }

}
