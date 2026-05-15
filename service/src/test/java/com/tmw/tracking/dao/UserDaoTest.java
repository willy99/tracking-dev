package com.tmw.tracking.dao;

import com.tmw.tracking.entity.User;
import com.tmw.tracking.utils.PasswordGenerator;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

@Category(TrackingBaseUnitTest.class)
public class UserDaoTest extends TrackingBaseUnitTest {

    public static final String TEST_EXAMPLE_COM = "test@example.com";
    private EntityManager entityManager;
    private UserDao userDao;
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
    }


    @Test
    public void testUsersNotInRole(){

    }

    @Test
    public void testCRUDForUser() {
        String currentLast = ""+System.currentTimeMillis();
        User user = new User();
        user.setActive(true);
        user.setEmail(TEST_EXAMPLE_COM);
        user.setFirstName("first");
        user.setLastName("last");
        user.setLastName(currentLast);
        user.setPassword(PasswordGenerator.encryptPassword("kuku"));
        User existing = userDao.getAnyUserByCredentials(TEST_EXAMPLE_COM);
        if (existing != null) {
            user = existing;
            user.setLastName(currentLast);
            user.setActive(true);
            userDao.update(user);
        } else {
            userDao.create(user);
        }

        User created = userDao.getUserByCredentials(TEST_EXAMPLE_COM);
        assertNotNull(created);
        assertEquals(created.getLastName(), currentLast);

        userDao.delete(user);

        User deleted = userDao.getAnyUserByCredentials(TEST_EXAMPLE_COM);
        assertNotNull(deleted);
        assertFalse(deleted.isActive());

    }


}
