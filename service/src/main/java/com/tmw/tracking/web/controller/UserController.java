package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Permission;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.support.RoleInfo;
import com.tmw.tracking.entity.support.UserInfo;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.utils.DomainUtils;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.aop.MethodCall;
import com.tmw.tracking.web.service.exception.NotFoundException;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/userstore")
@Singleton
public class UserController extends BaseController {

    private final UserService userService;
    private final RoleDao roleDao;
    private final PermissionService permissionService;
    private final AuthenticationService authenticationService;
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    public UserController(final UserService userService,
                          final PermissionService permissionService,
                          final RoleDao roleDao,
                          final AuthenticationService authenticationService) {
        this.userService = userService;
        this.permissionService = permissionService;
        this.roleDao = roleDao;
        this.authenticationService = authenticationService;
    }

    @GET
    @Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
    @Path("/modalSearchUser")
    public Viewable getModalSearchUser() {return new Viewable("/userstore/modalSearchUser");
    }


    @POST
    @Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
    @Path("/addRoleForUser")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_USERS)
    public String addRoleForUser(@QueryParam("roleId") Long roleId,
                                 @QueryParam("userId") Long userId){

        final Map<String, Object> vars = new HashMap<String, Object>();

        try {
            Role role = permissionService.getRoleById(roleId);
            if(role!=null){
              User user =  userService.addRoleForUser(userId, role);
              if(user!=null) {
                  vars.put("succeed", "Role added for user");
              }else{
                  vars.put("cancelled", "Role NOT added for user");
              }
            }
        }catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during adding Role for user" + e.getMessage());
        }
         return Utils.toJson(vars);
    }


    @GET
    @Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
    @Path("/profile")
    @MethodCall(requiredPermission = PermissionType.LOGIN_APP)
    public Viewable getUserProfile() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("environment", environment);
        return new Viewable("/userstore/profile", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML + ";charset=utf-8")
    @Path("/roleManagement")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_ROLES)
    public Viewable getRoleManagement() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("environment", environment);
        vars.put("pageSize", Page.ITEMS_ON_PAGE);
        return new Viewable("/userstore/roleManagement", vars);
    }



   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/getUsersByRole")
   public List<User> getUsersByRole(@QueryParam("roleId")@NotNull Long roleId){
       return userService.getUsersByRole(roleId);
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllRoles")
    public List<Role> getAllRoles(@QueryParam("searchFor") @NotNull String matcher,
                                  @Context UriInfo uriInfo,
                                  @Context HttpServletResponse response) {

        final Page<Role> page = permissionService.getAllRolesForWeb(new SearchCriteria(matcher, getPageQuery(uriInfo)));
        setPageHeaders(page, response);
        return page.getContent();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllPermissions")
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getRole")
    public RoleInfo getRole(@QueryParam("id") final Long id) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRole(permissionService.getRoleById(id));
        roleInfo.setPermissions(roleInfo.getRole().getPermissionList());
        roleInfo.setAllPermissions(permissionService.getAllPermissions());
        return roleInfo;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getRoleByName")
    public RoleInfo getRoleByName(@QueryParam("roleName") final String roleName) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRole(permissionService.getRoleByName(roleName));
        roleInfo.setPermissions(roleInfo.getRole().getPermissionList());
        roleInfo.setAllPermissions(permissionService.getAllPermissions());
        return roleInfo;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/saveRole")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_ROLES)
    public String saveRole(RoleInfo roleInfo) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if (roleInfo.getRole() == null) {
            vars.put("errorMessage", "Role is empty");
            return Utils.toJson(vars);
        }

        Role role = roleInfo.getRole().getId() != null ? permissionService.getRoleById(roleInfo.getRole().getId()): new Role();
        role.setRoleName(roleInfo.getRole().getRoleName());
        role.setPermissionList(roleInfo.getPermissions());
        try {
            permissionService.updateRole(role);
            permissionService.init();
        } catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during saving Role " + e.getMessage());
        }
        return Utils.toJson(vars);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteRole")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_ROLES)
    public String deleteRole(RoleInfo roleInfo) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if (roleInfo.getRole() == null) {
            vars.put("errorMessage", "role is empty");
            return Utils.toJson(vars);
        }

        Role role = permissionService.getRoleById(roleInfo.getRole().getId());
        if (role == null) {
            vars.put("errorMessage", "role is not found");
            return Utils.toJson(vars);
        }
        try {
            permissionService.deleteRole(role);
        } catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during deleting Role " + e.getMessage());
        }
        return Utils.toJson(vars);
    }




    /********** USER AREA ********/

        @GET
    @Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
    @Path("/userManagement")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_USERS)
    public Viewable getUserManagement() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("roles", permissionService.getAllRoles(false));
        vars.put("pageSize", Page.ITEMS_ON_PAGE);
        vars.put("environment", environment);
        return new Viewable("/userstore/userManagement", vars);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_USERS)
    public UserInfo getUser(@QueryParam("id") final Long id) {
        if (id == null)
            throw new ValidationException("ID cannot be null.");
        final User user = userService.getById(id);
        if (user == null)
            throw new NotFoundException("User was not found.");
        return new UserInfo(user);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/findUsers")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_USERS)
    public List<User> findUsers(SearchCriteria searchCriteria,
                                @Context UriInfo uriInfo,
                                @Context HttpServletResponse response) {


        //processSearchCriteria(searchCriteria);

        final Page<User> page = userService.find(getPageQuery(uriInfo), searchCriteria);
        setPageHeaders(page, response);
        return page.getContent();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/findUsersNotInRole")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_USERS)
    public List<User> findUsersNotInRole(SearchCriteria searchCriteria,
                                @Context UriInfo uriInfo,
                                @Context HttpServletResponse response) {

        processSearchCriteria(searchCriteria);

        final Page<User> page = userService.findUserNotInRole(getPageQuery(uriInfo), searchCriteria);
        setPageHeaders(page, response);
        return page.getContent();
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllUsers")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_USERS)
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/find")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_USERS)
    public String findUser(@QueryParam("id") final Long id) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if (id == null) {
            vars.put("errorMessage", "Incorrect id");
            return Utils.toJson(vars);
        }
        // fix me should be visibility
        User user = userService.getById(id);
        if (user == null) {
            vars.put("errorMessage", "User not found");
            return Utils.toJson(vars);
        }
        vars.put("user", user);
        return Utils.toJson(vars);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/getProfile")
    @MethodCall(requiredPermission = PermissionType.LOGIN_APP)
    public String getProfile() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();

        vars.put("user", new UserInfo(userService.getById(authenticatedUser.getId())));
        return Utils.toJson(vars);
    }


    /*@GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/switch")
    public String deleteUser(@QueryParam("email") final String email, @QueryParam("mode") final Boolean mode) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        User user = userService.getAnyUserByCredentials(email);
        if (user == null) {
            vars.put("errorMessage", "User was not found: " + email);
        } else {
            if (user.equals(AuthenticationServiceImpl.getAuthenticatedUser())) {
                vars.put("errorMessage", "User can't edit himself");
                return Utils.toJson(vars);
            }
            user.setActive(mode);
            userService.update(user);
        }
        return Utils.toJson(vars);
    }*/

    /*@GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/clearRole")
    public String clearUserRole(@QueryParam("email") final String email) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        User user = userService.clearRole(email);
        if (user == null) {
            vars.put("errorMessage", "User was not found: " + email);
        } else {
            if (user.equals(AuthenticationServiceImpl.getAuthenticatedUser())) {
                vars.put("errorMessage", "User can't edit himself");
                return Utils.toJson(vars);
            }
        }

        vars.put("user", user);
        return Utils.toJson(vars);
    }*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGE_USERS)
    public User saveUser(UserInfo userFromClient) {
        User user = userFromClient.convert(roleDao);
        if (userFromClient.getId() == null) {
            return userService.create(user);
        } else {
            if (userFromClient.getPassword().isEmpty()) {
                user.setPassword(userFromClient.getOldPassword());
            } else {
                authenticationService.passwordWorkflowForUpdateUser(user, userFromClient.getPassword());
            }
            return userService.update(user);
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/saveProfile")
    @MethodCall(requiredPermission = PermissionType.LOGIN_APP)
    public User saveProfile(UserInfo userFromClient) {
        User user = userFromClient.convert(roleDao);
        if (userFromClient.getPassword().isEmpty()) {
            user.setPassword(userFromClient.getOldPassword());
        } else {
            authenticationService.passwordWorkflowForUpdateUser(user, userFromClient.getPassword());
        }
        user.setTenant(DomainUtils.getAuthenticatedUser().getTenant());
        DomainUtils.getAuthenticatedUser().setLocale(user.getLocale());
        return userService.updateProfile(user);
    }


    private void processSearchCriteria(SearchCriteria searchCriteria) {
        if (searchCriteria != null) {
            if (searchCriteria.getRoleId() != -1) {
                Role role = permissionService.getRoleById(searchCriteria.getRoleId());
                if (role != null) {
                    searchCriteria.addAdditionalFilters("role", role.getRoleName());
                }
            }

            if (searchCriteria.getActivity() != null) {
                searchCriteria.addAdditionalFilters("activity", searchCriteria.getActivity());
            }
        }
    }




}
