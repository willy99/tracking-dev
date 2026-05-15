package com.tmw.tracking.domain.flex.dao;

import com.tmw.tracking.domain.flex.entities.FlexHistory;
import com.tmw.tracking.domain.flex.to.FlexHistoryTO;

import java.util.Date;
import java.util.List;

public interface FlexHistoryDao {

    void batchAdd(List<FlexHistory> historyList);
    List<FlexHistoryTO> getHistory(String searchString, Date lastUpdated);

}
