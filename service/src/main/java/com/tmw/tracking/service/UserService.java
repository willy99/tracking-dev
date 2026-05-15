package com.tmw.tracking.service;

import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;

import java.util.List;

public interface UserService {

    String SYSTEM_ADMIN_MESSAGE = "User is system admin";
    String INCORRECT_CREDENTIALS_MESSAGE = "Incorrect credentials";
    String USER_NOT_FOUND_MESSAGE = "User is not found";

    /**
     * Method checks if login credentials are correct and user role is one of store admin/regional admin/system admin
     * @return instance of CredentialsStatus
     */
    CredentialsStatus validateUserCredentials(LoginRequest loginRequest);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    User getAnyUserByCredentials(String email);

    User getById(Long id);

    User update(User user);

    List<User> getUsersByRole(Long roleId);

    User create(User user);

    Page<User> find(PageQuery pageQuery, SearchCriteria searchCriteria);

    User addRoleForUser(Long userId, Role role);


    Page<User> findUserNotInRole(PageQuery pageQuery, SearchCriteria searchCriteria);

    User getCompanyAdmin(Company company);

    void sendRecoverLink(LoginRequest loginRequest);

    User updateProfile(User userFromClient);

    User getByActivationHash(String hash);

    /**
     * only password field updating (activation process)
     */
    User updatePassword(User user);


    /**
     * Class contains info about credentials processing
     */
    class CredentialsStatus {
        boolean status;
        String message;

        public CredentialsStatus(boolean status, String message) {
            this.status = status;
            this.message = message;
        }

        public boolean isStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
