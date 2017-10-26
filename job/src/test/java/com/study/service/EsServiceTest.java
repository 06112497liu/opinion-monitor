/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.study.service;

import com.bbd.service.EsService;
import com.bbd.service.vo.OpinionEsVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: EsServiceTest.java, v 0.1 2017/10/25 0025 18:00 tjwang Exp $
 */
public class EsServiceTest extends BaseServiceTest {

    @Autowired
    private EsService esService;

    @Test
    public void testCreateIndex() {
        esService.createIndex("bbd_opinion_a");
    }

    @Test
    public void testSyncOpinions() {
        esService.syncOpinions("bbd_opinion_a");
    }

    @Test
    public void testSyncOpinionToNewIndex() {
        String newIndex = "bbd_opinion_b";
        String oldIndex = "bbd_opinion_a";
        esService.syncOpinionToNewIndex(newIndex, oldIndex);
    }

    @Test
    public void testSearchOpinions() {
        List<OpinionEsVO> ds = esService.searchOpinions();
        logger.info("size : " + ds.size());
    }

    @Test
    public void testSearchOpinionAggs() {
        esService.searchOpinionAggs();
    }

    @Test
    public void testGetAlias() {
        esService.getAlias();
    }

}
