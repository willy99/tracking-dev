package com.tmw.tracking.dao;

import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;

import java.util.List;


public interface UserDao {

    User getById(Long id);

    User getUserByCredentials(String credentials);

    User getAnyUserByCredentials(String credentials);

    List<User> getUsersByRole(Long roleId);

    User create(User user);

    User update(User user);

    void delete(User user);

    List<User> getAll();

    User clearRole(String email);

    Page<User> find(PageQuery pageQuery, SearchCriteria searchCriteria);

    User addRoleForUser(Long userId, Role role);

    Page<User> findUserNotInRole(PageQuery pageQuery, SearchCriteria searchCriteria);

    User getCompanyAdmin(Company company);

    List<User> getByTenant(Company company);

    User getByActivationHash(String activationHash);

}
