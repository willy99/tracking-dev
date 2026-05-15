package com.tmw.tracking.web.service.auth;

import com.google.inject.Singleton;
import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.domain.LoginResponse;
import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.entity.Permission;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.utils.DynamicConfig;
import com.tmw.tracking.web.service.exception.ServiceException;
import com.tmw.tracking.web.service.exception.ValidationException;
import com.tmw.tracking.web.service.util.error.ErrorCode;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/auth")
@Singleton
public class AuthenticationResource {

    private final AuthenticationService authenticationLogic;
    private final DynamicConfig dynamicConfig;
    private final String clientVersion;
    private final PermissionService permissionService;

    @Inject
    public AuthenticationResource(final AuthenticationService authenticationLogic, PermissionService permissionService, DynamicConfig dynamicConfig, @Named("client.version") String clientVersion) {
        this.authenticationLogic = authenticationLogic;
        this.dynamicConfig = dynamicConfig;
        this.clientVersion = clientVersion;
        this.permissionService = permissionService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public LoginResponse login(final LoginRequest loginRequest) {
        validateLoginRequest(loginRequest);
        final AuthenticatedUser authenticatedUser = authenticationLogic.login(loginRequest.getUserId().toUpperCase(), loginRequest.getPassword());
        LoginResponse loginResponse = new LoginResponse(authenticatedUser);

        Role currentRole = authenticatedUser.getUser().getRole();
        List<String> permissions = new ArrayList<>();
        for (Permission permission: permissionService.getPermissions(currentRole)) {
            permissions.add(permission.getName().name());
        }
        loginResponse.setPermissions(permissions);
        loginResponse.setRole(currentRole.getRoleName());
        return loginResponse;
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/logout")
    public Object logout(@QueryParam("token") final String token) {
        if (token == null)
            throw new ServiceException("Token cannot be null.", ErrorCode.AUTH_ERROR_TOKEN_IS_INVALID);
        authenticationLogic.logout(token);
        return null/* return nothing */;
    }


    // ------------------------------------------------------------------------

    /**
     * Validate {@link LoginRequest}
     *
     * @param loginRequest the {@code LoginRequest}. Can be {@code null}
     */
    private void validateLoginRequest(final LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new ValidationException("Login request cannot be null.");
        }
        if (StringUtils.isBlank(loginRequest.getUserId())) {
            throw new ValidationException("User ID must be specified.");
        }
    }
}
