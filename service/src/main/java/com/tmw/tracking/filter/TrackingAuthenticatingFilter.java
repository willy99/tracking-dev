package com.tmw.tracking.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.service.util.response.ServiceResponse;
import com.tmw.tracking.web.service.util.response.Status;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

public class TrackingAuthenticatingFilter extends AuthenticatingFilter {
    private final static Logger logger = LoggerFactory.getLogger(TrackingAuthenticatingFilter.class);

    /**
     * {@inheritDoc}
     * @see
     */
    @Override
    protected AuthenticationToken createToken(final ServletRequest request, final ServletResponse response) throws Exception {
        try {
            final HttpServletRequest servletRequest = (HttpServletRequest) request;
            String token = null;
            if("DELETE".equals(servletRequest.getMethod()) || "POST".equals(servletRequest.getMethod())) {
                final String[] pathParts = servletRequest.getRequestURI().split("\\/");
                token = pathParts[pathParts.length - 1];
            } else if("GET".equals(((HttpServletRequest)request).getMethod())) {
                token = request.getParameter("token");
            }
            return new TrackingAuthenticationToken(token);
        } catch (Exception e){
            logger.error(Utils.errorToString(e));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatingFilter#onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatingFilter#onLoginFailure(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationException, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    protected boolean onLoginFailure(final AuthenticationToken token, final AuthenticationException e,
                                     final ServletRequest request, final ServletResponse response) {
        final ObjectMapper objectMapper = Utils.initObjectMapper();
        try {
            //if (e instanceof AuthenticationException) {
                ((HttpServletResponse)response).setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
                objectMapper.writeValue(response.getWriter(), new ServiceResponse(Status.ERROR, new com.tmw.tracking.web.service.util.response.Error(e.getMessage(), "401")));
            /*} else if(e.getCause() instanceof ServiceException){
                final ServiceException error = (ServiceException)e.getCause();
                objectMapper.writeValue(response.getWriter(), new ServiceResponse(Status.ERROR, new com.tmw.tracking.web.service.util.response.Error((String)error.getResponse().getEntity(), error.getCode())));
            } else if(e.getCause() != null) {
                logger.error(Utils.errorToString(e.getCause()));
                objectMapper.writeValue(response.getWriter(), new ServiceResponse(Status.ERROR, new com.tmw.tracking.web.service.util.response.Error("Internal Server Error", "500")));
            }*/
        } catch (Exception error){
            logger.error(Utils.errorToString(error));
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatingFilter#cleanup(javax.servlet.ServletRequest, javax.servlet.ServletResponse, Exception)
     */
    /*@Override
    protected void cleanup(final ServletRequest request, final ServletResponse response, final Exception existing)
            throws ServletException, IOException {
        super.cleanup(request, response, existing);
        SecurityUtils.getSubject().logout();
    }*/
}
