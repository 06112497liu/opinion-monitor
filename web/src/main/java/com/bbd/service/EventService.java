package com.bbd.service;

import com.bbd.domain.OpinionEvent;

public interface EventService {
	
	void createEvent(OpinionEvent opinionEvent);
	
	void modifyEvent(OpinionEvent opinionEvent);
	
	OpinionEvent getEvent(long id);
}
