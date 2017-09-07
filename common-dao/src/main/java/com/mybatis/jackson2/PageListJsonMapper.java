package com.mybatis.jackson2;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mybatis.domain.PageList;

/**
 * @author miemiedev
 */
public class PageListJsonMapper extends ObjectMapper {

    private static final long serialVersionUID = -5503914938769017719L;

    public PageListJsonMapper() {
        SimpleModule module = new SimpleModule("PageListJSONModule", new Version(1, 0, 0, null, null, null));
        module.addSerializer(PageList.class, new PageListJsonSerializer(this));
        registerModule(module);
    }
}
