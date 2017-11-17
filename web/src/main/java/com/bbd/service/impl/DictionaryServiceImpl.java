package com.bbd.service.impl;

import com.bbd.dao.OpinionDictionaryDao;
import com.bbd.domain.OpinionDictionary;
import com.bbd.domain.OpinionDictionaryExample;
import com.bbd.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Liuweibo
 * @version Id: DictionaryServiceImpl.java, v0.1 2017/11/17 Liuweibo Exp $$
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private OpinionDictionaryDao dictionaryDao;

    @Override
    public Map<String, String> queryDictionary(String type) {
        OpinionDictionaryExample example = new OpinionDictionaryExample();
        example.createCriteria().andParentEqualTo(type);
        List<OpinionDictionary> list = dictionaryDao.selectByExample(example);
        Map<String, String> map = list.stream().collect(Collectors.toMap(OpinionDictionary::getCode, OpinionDictionary::getName));
        return map;
    }
}
    
    