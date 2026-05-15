package com.tmw.tracking.dao;

import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.enums.RoleEnum;
import com.tmw.tracking.entity.support.UserInfo;
import com.tmw.tracking.service.CompanyService;
import com.tmw.tracking.utils.PasswordGenerator;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

import static junit.framework.Assert.assertNotNull;

@Category(TrackingBaseUnitTest.class)
public class CompanyDaoTest extends TrackingBaseUnitTest {

    static final String TEST_EXAMPLE_COM = "test@example.com";
    private EntityManager entityManager;
    private UserDao userDao;
    private CompanyDao companyDao;
    private RoleDao roleDao;
    private CompanyService companyService;
    private final static Logger logger = LoggerFactory.getLogger(UserDaoTest.class);

    /**
     * {@inheritDoc}
     * @see TrackingBaseUnitTest#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        entityManager = injector.getInstance(EntityManager.class);
        userDao = injector.getInstance(UserDao.class);
        companyDao = injector.getInstance(CompanyDao.class);
        companyService = injector.getInstance(CompanyService.class);
        roleDao = injector.getInstance(RoleDao.class);
    }


    @Test
    public void testCRUDForUser() {
        String currentLast = ""+System.currentTimeMillis();
        Company company = new Company();
        company.setName("test");
        company.setActive(true);
        User user = new User();
        user.setActive(true);
        user.setEmail(TEST_EXAMPLE_COM + currentLast);
        user.setFirstName("first");
        user.setLastName("last");
        user.setPhone("9" + currentLast);
        user.setLastName(currentLast);
        user.setPassword(PasswordGenerator.encryptPassword("kuku"));
        user.setRole(roleDao.getByRoleName(RoleEnum.COMPANY_ADMIN.getName()));

        company.setAdmin(new UserInfo(user));

        companyService.create(company);

        Company created = companyService.getById(company.getId());
        assertNotNull(created);

        entityManager.remove(user);
        entityManager.remove(company);

    }

}
