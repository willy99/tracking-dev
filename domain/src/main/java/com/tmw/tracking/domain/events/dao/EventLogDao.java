package com.tmw.tracking.domain.events.dao;

import com.tmw.tracking.domain.events.entities.EventLog;
import com.tmw.tracking.domain.events.to.EventLogSearchFilterTO;
import com.tmw.tracking.domain.events.to.EventLogTO;

import java.util.List;

public interface EventLogDao {

    void log(EventLog event);

    List<EventLogTO> search(EventLogSearchFilterTO filter);
}
