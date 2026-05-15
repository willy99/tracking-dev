package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.support.UserInfo;
import com.tmw.tracking.service.CompanyService;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.web.aop.MethodCall;
import com.tmw.tracking.web.service.exception.NotFoundException;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/company")
@Singleton
public class CompanyController extends BaseController {

    private final CompanyService companyService;
    private final UserService userService;
    private final PermissionService permissionService;
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    public CompanyController(
            final CompanyService companyService,
            final PermissionService permissionService,
            final UserService userService) {
        this.companyService = companyService;
        this.permissionService = permissionService;
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
    @Path("/companyManagement")
    @MethodCall(requiredPermission = PermissionType.COMPANIES_MAINTAIN)
    public Viewable getCompanyManagement() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("pageSize", Page.ITEMS_ON_PAGE);
        vars.put("environment", environment);
        return new Viewable("/company/companyManagement", vars);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/find")
    @MethodCall(requiredPermission = PermissionType.COMPANIES_MAINTAIN)
    public List<Company> findCompanies(SearchCriteria searchCriteria,
                                @Context UriInfo uriInfo,
                                @Context HttpServletResponse response) {
        processSearchCriteria(searchCriteria);

        final Page<Company> page = companyService.find(getPageQuery(uriInfo), searchCriteria);
        setPageHeaders(page, response);
        return page.getContent();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get")
    @MethodCall(requiredPermission = PermissionType.COMPANIES_MAINTAIN_CRUD)
    public Company getCompany(@QueryParam("id") final Long id) {
        if (id == null) {
            throw new ValidationException("Incorrect id");
        }
        Company company = companyService.getById(id);
        if (company == null) {
            throw new NotFoundException("Company not found by id " + id);
        }
        //assign admin info
        company.setAdmin(new UserInfo(userService.getCompanyAdmin(company)));
        return company;
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAll")
    @MethodCall(requiredPermission = PermissionType.COMPANIES_MAINTAIN_CRUD)
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    @MethodCall(requiredPermission = PermissionType.COMPANIES_MAINTAIN_CRUD)
    public Company saveCompany(Company uiCompany) {
        Company company = new Company();
        company.setId(uiCompany.getId());
        company.setName(uiCompany.getName());
        company.setActive(uiCompany.isActive());
        company.setAdmin(uiCompany.getAdmin());
        company.setVersion(uiCompany.getVersion());
        if (company.getId() == null) {
            return companyService.create(company);
        } else {
            return companyService.update(company);
        }
    }


    /* ************* ************ ***********
     * Web Statistic
     * @param searchCriteria
     */


    private void processSearchCriteria(SearchCriteria searchCriteria) {
        if (searchCriteria != null) {
            if (searchCriteria.getActivity() != null) {
                searchCriteria.addAdditionalFilters("activity", searchCriteria.getActivity());
            }
        }
    }




}
