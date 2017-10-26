/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.task;

import com.bbd.domain.TaskEntity;
import com.bbd.service.EsService;
import com.bbd.service.OpinionService;
import com.bbd.service.TaskService;
import com.bbd.service.vo.OpinionEsVO;
import com.bbd.util.EsUtil;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.mybatis.domain.PageBounds;
import com.mybatis.domain.PageList;
import com.mybatis.domain.SortBy;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: OpinionEsDataSyncTaskExecutor.java, v 0.1 2017/10/26 0026 14:42 tjwang Exp $
 */
@Component
public class OpinionEsDataSyncTaskExecutor extends AbstractTaskExecutor<String, OpinionEsVO> {

    private volatile boolean running = false;

    private Long             taskId;

    private String           newIndex;

    private String           oldIndex;

    private String           indexAlias;

    private String           type;

    @Autowired
    private TaskService      taskService;

    @Autowired
    private OpinionService   opinionService;

    @Autowired
    private EsService        esService;

    public OpinionEsDataSyncTaskExecutor() {
        this.taskId = 1L;
        this.newIndex = "bbd_opinion_a";
        this.oldIndex = "bbd_opinion_b";
        this.indexAlias = "bbd_opinion";
        this.type = "opinion";
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public String getCurrentItem() {
        TaskEntity t = taskService.getById(taskId);
        return t.getCurrentItem();
    }

    @Override
    public void setCurrentItem(List<OpinionEsVO> datas) {
        String uuid = datas.get(datas.size() - 1).getUuid();
        TaskEntity t = taskService.getById(taskId);
        t.setCurrentItem(uuid);
        t.setGmtModified(new Date());
        taskService.updateTask(t);
    }

    @Override
    public List<OpinionEsVO> getData(String item) {
        PageBounds pb = new PageBounds(1, 2);
        return opinionService.queryOpinionLessThanUUID(item, pb);
    }

    @Override
    public void doWork(List<OpinionEsVO> datas) {
        EsUtil.create(newIndex, type, datas);
    }

    @Override
    public void start() {
        TaskEntity task = taskService.getById(taskId);
        String curItem = task.getCurrentItem();
        if (StringUtils.isBlank(curItem)) {
            esService.createIndex(newIndex);
            if (task.getBeginTime() == null) {
                task.setBeginTime(new Date());
                taskService.updateTask(task);
            }
        } else {
            Optional<OpinionEsVO> lastOpt = getLastOpinion(curItem);
            if (lastOpt.isPresent()) {
                String uuid = lastOpt.get().getUuid();
                task.setCurrentItem(uuid);
                taskService.updateTask(task);
            }
        }

        running = true;
    }

    /**
     * 获取新的ES索引中ID最小的数据
     * @param curItem
     * @return
     */
    private Optional<OpinionEsVO> getLastOpinion(String curItem) {
        QueryBuilders.rangeQuery("uuid").lte(curItem);
        PageBounds pb = new PageBounds(1, 1);
        pb.setOrders(Lists.newArrayList(SortBy.create("uuid.keyword", SortBy.Direction.ASC.toString())));
        PageList<OpinionEsVO> list = EsUtil.search(newIndex, type, QueryBuilders.matchAllQuery(), pb, OpinionEsVO.class);
        return Optional.of(list.get(0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void end() {
        TaskEntity task = taskService.getById(taskId);
        Date now = new Date();
        task.setEndTime(now);
        task.setGmtModified(now);
        taskService.updateTask(task);

        esService.changeOpinionAlias(newIndex, oldIndex, indexAlias);
        esService.deleteIndex(oldIndex);

        running = false;
    }

    @Override
    public void stop() {

    }

}
