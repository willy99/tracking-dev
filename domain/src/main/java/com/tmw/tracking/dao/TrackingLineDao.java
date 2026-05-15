package com.tmw.tracking.dao;

import com.tmw.tracking.entity.TrackingLine;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;

import java.util.List;

public interface TrackingLineDao {

    TrackingLine getById(final Long id);

    TrackingLine create(TrackingLine trackingLine);

    List<TrackingLine> getAll();

    Page<TrackingLine> getByPage(PageQuery pageQuery);

}