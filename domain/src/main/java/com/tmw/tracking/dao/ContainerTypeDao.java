package com.tmw.tracking.dao;

import com.tmw.tracking.Transaction;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;

import java.util.List;

/**
 * Created by pzhelnov on 3/6/2017.
 */
public interface ContainerTypeDao {

    ContainerType getById(Long id);

    List<ContainerType> getAll();

    Page<ContainerType> getByPage(PageQuery pageQuery);

    @Transaction
    ContainerType create(ContainerType containerType);

    ContainerType update(ContainerType containerType);

    void delete(ContainerType containerType);

}
