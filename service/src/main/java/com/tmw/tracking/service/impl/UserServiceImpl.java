package com.tmw.tracking.service.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;
import com.tmw.tracking.mail.MailSender;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.web.service.exception.NotFoundException;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.List;

@Singleton
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final MailSender mailSender;
    private final AuthenticationService authenticationService;

    @Inject
    public UserServiceImpl(final UserDao userDao,
                           final MailSender mailSender,
                           final AuthenticationService authenticationService) {
        this.userDao = userDao;
        this.mailSender = mailSender;
        this.authenticationService = authenticationService;
    }

    @Override
    public CredentialsStatus validateUserCredentials(LoginRequest loginRequest) {

        if (loginRequest == null) {
            throw new ValidationException("Login request cannot be null.");
        }
        if (loginRequest.getUserId() == null || StringUtils.isBlank(loginRequest.getUserId())) {
            throw new ValidationException("User ID must be specified.");
        }
        if (loginRequest.getPassword() == null || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new ValidationException("Password must be specified.");
        }
        String userId = loginRequest.getUserId();
        //String password = loginRequest.getPassword();


        User user = userDao.getUserByCredentials(userId.toUpperCase());
        if (user == null) {
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return new CredentialsStatus(true, "OK");
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByCredentials(email);
    }

    @Override
    public User getAnyUserByCredentials(String email) {
        return userDao.getAnyUserByCredentials(email);
    }

    @Override
    public User getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public List<User> getUsersByRole(Long roleId){return userDao.getUsersByRole(roleId);}

    @Override
    public User update(User user) {
        validateUser(user, false);
        //TODO passwordworflow
        return userDao.update(user);
    }

    @Override
    public User create(User user) {
        validateUser(user, false);
        authenticationService.passwordWorkflowForNewUser(user, true);
        user.setLocale(AuthenticationServiceImpl.getAuthenticatedUser().getLocale()); //admin's default locale
        //TODO maybe company defaults?
        return userDao.create(user);
    }

    private void validateUser(User user, boolean profile) {
        if (user.getPhone() == null || user.getEmail() == null) {
            throw new ValidationException("Email or phone is empty!");
        }
        User existing = getAnyUserByCredentials(user.getEmail());

        if ( (existing != null && user.getId() == null) ||
                (user.getId() != null && existing != null && !existing.getId().equals(user.getId())) ) {
            throw new ValidationException("User with the email " + user.getEmail() + " already exists (it might be inactive)");
        }

        existing = getAnyUserByCredentials(user.getPhone());

        if ( (existing != null && user.getId() == null) ||
                (user.getId() != null && existing != null && !existing.getId().equals(user.getId())) ) {
            throw new ValidationException("User with the phone " + user.getPhone() + " already exists (it might be inactive)");
        }

        if (user.equals(AuthenticationServiceImpl.getAuthenticatedUser()) && !profile) {
            throw new ValidationException("User can't edit himself");
        }

        if (profile && existing != null) {
            //check for updating roles
            if (existing.getRole()!=null && !existing.getRole().equals(user.getRole())) {
                throw new ValidationException("Can't change role for profile!");
            }
        }
    }


    @Override
    public Page<User> find(PageQuery pageQuery, SearchCriteria searchCriteria) {
        return userDao.find(pageQuery, searchCriteria);
    }

    @Override
    public User addRoleForUser(Long userId, Role role) {
        return userDao.addRoleForUser(userId, role);
    }

    @Override
    public Page<User> findUserNotInRole(PageQuery pageQuery, SearchCriteria searchCriteria) {
        return userDao.findUserNotInRole(pageQuery,searchCriteria);
    }

    //TODO @Admin
    @Override
    public User getCompanyAdmin(Company company) {
        return userDao.getCompanyAdmin(company);
    }

    @Override
    public void sendRecoverLink(LoginRequest loginRequest) {
        User user = getUserByEmail(loginRequest.getUserId());
        if (user == null) {
            throw new ValidationException("User is not found!");
        }

        String body = "<link>";
        mailSender.sendEmailMessage(user.getEmail(), "Recover the passowrd", body);

    }

    @Override
    public User updateProfile(User user) {
        validateUser(user, true);
        return userDao.update(user);

    }

    @Override
    public User getByActivationHash(String hash) {
        return userDao.getByActivationHash(hash);
    }

    @Override
    public User updatePassword(User user) {
        return userDao.update(user);
    }

}
