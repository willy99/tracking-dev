package com.tmw.tracking.dao;

import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;

import java.util.List;

/**
 * Created by pzhelnov on 11/20/2017.
 */
public interface CompanyDao {
    Company getById(Long id);

    Company getByName(String name);

    Company create(Company company);

    Company update(Company company);

    List<Company> getActive();

    Page<Company> find(PageQuery pageQuery, SearchCriteria searchCriteria);

}
