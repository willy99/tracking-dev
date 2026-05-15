package com.tmw.tracking.service;

import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;

import java.util.List;

/**
 * Created by pzhelnov on 11/20/2017.
 */
public interface CompanyService {

    List<Company> getAllCompanies();

    Company getCompanyByName(String name);

    Company getById(Long id);

    Company update(Company company);

    Company create(Company company);

    Page<Company> find(PageQuery pageQuery, SearchCriteria searchCriteria);

}
