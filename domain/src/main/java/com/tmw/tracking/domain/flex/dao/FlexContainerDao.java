package com.tmw.tracking.domain.flex.dao;

import com.tmw.tracking.domain.flex.entities.FlexContainer;
import com.tmw.tracking.domain.flex.to.FlexContainerTO;

import java.util.Date;
import java.util.List;

public interface FlexContainerDao {

    FlexContainer add(FlexContainer flexContainer);

    void upsertAll(List<FlexContainer> flexContainers);

    FlexContainer update(FlexContainer flexContainer);

    FlexContainer getContainerByNumber(String containerNum);

    List<FlexContainer> getContainerListByOrder(String orderNum);

    List<FlexContainer> getContainersByNumbers(List<String> containerNums);

    List<FlexContainerTO> getContainersForImport(String searchString, Date lastUpdated);

}
