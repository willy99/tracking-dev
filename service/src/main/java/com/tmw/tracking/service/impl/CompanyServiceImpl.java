package com.tmw.tracking.service.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.dao.CompanyDao;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;
import com.tmw.tracking.mail.MailSender;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.CompanyService;
import com.tmw.tracking.utils.PasswordGenerator;
import com.tmw.tracking.web.service.exception.ValidationException;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by pzhelnov on 11/20/2017.
 */
@Singleton
public class CompanyServiceImpl implements CompanyService {

    private final CompanyDao companyDao;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final MailSender mailSender;
    private final AuthenticationService authenticationService;


    @Inject
    public CompanyServiceImpl(
            final CompanyDao companyDao,
            final UserDao userDao,
            final MailSender mailSender,
            final RoleDao roleDao,
            final AuthenticationService authenticationService) {
        this.companyDao = companyDao;
        this.userDao = userDao;
        this.mailSender = mailSender;
        this.roleDao = roleDao;
        this.authenticationService = authenticationService;
    }


    @Override
    public List<Company> getAllCompanies() {
        return companyDao.getActive();
    }

    @Override
    public Company getCompanyByName(String name) {
        return companyDao.getByName(name);
    }

    @Override
    public Company getById(Long id) {
        return companyDao.getById(id);
    }

    @Override
    public Company update(Company company) {
        validateCompany(company);

        //check if company becomes inactive - need to throw all sessions
        if (!company.isActive()) {
            authenticationService.deactivateCompany(company);
        }
        Company updated = companyDao.update(company);
        User admin = userDao.getById(company.getAdmin().getId());
        if (company.getAdmin().getPassword() != null && !company.getAdmin().getPassword().isEmpty() && !(PasswordGenerator.encryptPassword(company.getAdmin().getPassword()).equals(company.getAdmin().getOldPassword()))) {
            authenticationService.passwordWorkflowForUpdateUser(admin, admin.getPassword());
        } else {
            admin.setPassword(company.getAdmin().getOldPassword());
        }
        userDao.update(admin);

        return updated;

    }

    @Override
    public Company create(Company company) {
        validateCompany(company);

        Company created = companyDao.create(company);

        User admin = userDao.getById(created.getAdmin().getId());
        authenticationService.passwordWorkflowForNewUser(admin, true);
        userDao.update(admin);
        return created;

    }

    @Override
    public Page<Company> find(PageQuery pageQuery, SearchCriteria searchCriteria) {
        return companyDao.find(pageQuery, searchCriteria);
    }

    private void validateCompany(Company company) {
        Company existing = companyDao.getByName(company.getName());

        if ( (existing != null && company.getId() == null) ||
                (company.getId() != null && existing != null && !existing.getId().equals(company.getId())) ) {
            throw new ValidationException("Company with the name " + company.getName() + " already exists (it might be inactive)");
        }
        User existingAdmin = userDao.getAnyUserByCredentials(company.getAdmin().getPhone());
        if (existingAdmin == null) {
            existingAdmin = userDao.getAnyUserByCredentials(company.getAdmin().getEmail());
        }
        if ((company.getId() == null && existingAdmin != null) ||
                (company.getAdmin().getId() != null && existingAdmin != null && !existingAdmin.getId().equals(company.getAdmin().getId()))) {
            throw new ValidationException("User with credentials " + company.getAdmin().getEmail() + " already exists (it might be inactive)");
        }

    }

}
