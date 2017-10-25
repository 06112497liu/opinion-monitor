package com.bbd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbd.dao.OpinionEventDao;
import com.bbd.domain.OpinionEvent;
import com.bbd.service.EventService;

@Service
public class EventServiceImpl implements EventService{

	@Autowired
	OpinionEventDao opinionEventDao;
	@Override
	public void createEvent(OpinionEvent opinionEvent) {
		opinionEventDao.insert(opinionEvent);
	}
	
	@Override
	public OpinionEvent getEvent(long id) {
		return opinionEventDao.selectByPrimaryKey(id);
	}

	@Override
	public void modifyEvent(OpinionEvent opinionEvent) {
		opinionEventDao.updateByPrimaryKey(opinionEvent);
	}

}
