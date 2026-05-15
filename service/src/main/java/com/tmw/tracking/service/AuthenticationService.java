package com.tmw.tracking.service;

import com.tmw.tracking.Transaction;
import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.User;

/**
 * Created by vandreev on 11/21/2014.
 */
public interface AuthenticationService {
    AuthenticatedUser login(String userId, String password);

    User validateUser(String token);

    @Transaction
    void logout(String token);

    void logout(User user);

    void deactivateCompany(Company company);

    void passwordWorkflowForNewUser(User user, Boolean generateNew);

    void passwordWorkflowForUpdateUser(User user, String newPassword);

    User checkHash(String hash);
}
