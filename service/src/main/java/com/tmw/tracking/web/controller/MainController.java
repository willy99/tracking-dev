package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.job.domain.JobInfo;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.web.service.exception.PermissionException;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/")
@Singleton
public class MainController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);

    private final String serverVersion;
    private AuthenticationService authenticationService;
    private UserService userService;

    @Inject
    public MainController(
            final AuthenticationService authenticationService,
            final UserService userService,
            @Named("server.version") final String serverVersion,
            @Named("major.version") final String majorVersion) {
        this.serverVersion = majorVersion+"_"+serverVersion;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    // ------------------------------------------------------------------------

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/status")
    public Viewable getStatusInfo() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("version", serverVersion);
        vars.put("environment", environment);
        final List<JobInfo> jobInfo = new ArrayList<JobInfo>();
        vars.put("jobInfo", jobInfo);
        vars.put("angular", true);
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        vars.put("locale", authenticatedUser.getLocale().getLanguage());
        return new Viewable("/status", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/login")
    public Viewable loginGet() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        return new Viewable("/login", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/recover")
    public Viewable recoverPage() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        vars.put("angular", true);
        return new Viewable("/anon/recover", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/activate/{hash}")
    public Viewable activatePage(@PathParam("hash") final String hash) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("userId", "");
        vars.put("error", "");
        try {
            authenticationService.checkHash(hash);
            vars.put("userId", hash);
        }
        catch (Exception ex) {
            vars.put("error", "The link is not validated");
        }
        vars.put("environment", environment);
        vars.put("angular", true);

        return new Viewable("/anon/activate", vars);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/recover")
    public User recover(final LoginRequest loginRequest) {
        User user = userService.getUserByEmail(loginRequest.getUserId());
        if (user == null) {
            throw new ValidationException("User with email is not found!");
        }
        authenticationService.passwordWorkflowForNewUser(user, false);
        userService.updatePassword(user);
        return user;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/activate")
    public User activate(final LoginRequest loginRequest) {

        User user = userService.getByActivationHash(loginRequest.getUserId());
        if (user == null) {
            throw new PermissionException("The activation link is incorrect or invalidated!");
        }
        authenticationService.passwordWorkflowForUpdateUser(user, loginRequest.getPassword());
        userService.updatePassword(user);
        return user;
    }



    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("/login")
    public Viewable loginPost(@Context final javax.servlet.http.HttpServletRequest req) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        vars.put("error", req.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME) != null);
        vars.put("username", req.getParameter("username"));
        vars.put("password", req.getParameter("password"));
        return new Viewable("/login", vars);
    }

}
