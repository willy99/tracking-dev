package com.tmw.tracking.web.service.company;

import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;
import com.tmw.tracking.service.CompanyService;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertNotNull;

@Category(TrackingBaseUnitTest.class)
public class CompanyServiceTest extends TrackingBaseUnitTest {

    private CompanyService companyService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        companyService = injector.getInstance(CompanyService.class);
    }

    @Test
    public void testFindCompanies() {
        final Page<Company> page = companyService.find(PageQuery.of(1), new SearchCriteria());
        assertNotNull(page);
    }

}
