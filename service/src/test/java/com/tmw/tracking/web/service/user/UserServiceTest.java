package com.tmw.tracking.web.service.user;

import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import com.tmw.tracking.web.service.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Category(TrackingBaseUnitTest.class)
public class UserServiceTest extends TrackingBaseUnitTest {

    private UserService userService;

    /**
     * {@inheritDoc}
     * @see TrackingBaseUnitTest#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        userService = injector.getInstance(UserService.class);
    }


    @Test(expected = ServiceException.class)
    public void testIncorrectCredentialsNullCredentials() {
        userService.validateUserCredentials(null);
    }

    @Test(expected = ServiceException.class)
    public void testIncorrectCredentialsEmptyUserId() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("bla");
        loginRequest.setUserId("");
        userService.validateUserCredentials(null);
    }

    @Test
    public void testIncorrectCredentialsEmptyPassword() {
        try {
            userService.validateUserCredentials(null);
        }
        catch (Exception e) {
            assertTrue(e.getMessage().contains("Login request cannot be null"));
        }
    }

    @Test
    public void testFindUsers() {
        final Page<User> page = userService.find(PageQuery.of(1), new SearchCriteria());
        assertNotNull(page);
    }
}
