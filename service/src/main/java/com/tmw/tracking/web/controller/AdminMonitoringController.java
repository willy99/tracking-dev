package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.domain.events.dao.EventLogDao;
import com.tmw.tracking.domain.events.to.ActiveSessionTO;
import com.tmw.tracking.domain.events.to.EventLogSearchFilterTO;
import com.tmw.tracking.domain.events.to.EventLogTO;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.web.aop.MethodCall;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/admin/monitoring")
@Singleton
public class AdminMonitoringController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AdminMonitoringController.class);

    private final EventLogDao eventLogDao;

    @Inject
    public AdminMonitoringController(final EventLogDao eventLogDao) {
        this.eventLogDao = eventLogDao;
    }

    @GET
    @Produces(MediaType.TEXT_HTML + ";charset=utf-8")
    @Path("/dashboard")
    @MethodCall(requiredPermission = PermissionType.SYSTEM_MONITORING)
    public Viewable getDashboard() {
        final Map<String, Object> vars = new HashMap<>();
        vars.put("angular", true);
        vars.put("environment", environment);
        return new Viewable("/admin/monitoring", vars);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/searchEvents")
    @MethodCall(requiredPermission = PermissionType.SYSTEM_MONITORING)
    public List<EventLogTO> searchEvents(@QueryParam("userEmail") String userEmail,
                                          @QueryParam("action") String action,
                                          @QueryParam("successOnly") Boolean successOnly,
                                          @QueryParam("dateFrom") Long dateFrom,
                                          @QueryParam("dateTo") Long dateTo) {
        EventLogSearchFilterTO filter = new EventLogSearchFilterTO();
        filter.setUserEmail(userEmail);
        filter.setAction(action);
        filter.setSuccessOnly(successOnly);
        filter.setDateFrom(dateFrom != null ? new Date(dateFrom) : null);
        filter.setDateTo(dateTo != null ? new Date(dateTo) : null);
        return eventLogDao.search(filter);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/activeSessions")
    @MethodCall(requiredPermission = PermissionType.SYSTEM_MONITORING)
    public List<ActiveSessionTO> getActiveSessions() {
        List<ActiveSessionTO> result = new ArrayList<>();
        Object securityManager = SecurityUtils.getSecurityManager();
        if (!(securityManager instanceof DefaultWebSecurityManager)) {
            return result;
        }
        DefaultWebSecurityManager webSecurityManager = (DefaultWebSecurityManager) securityManager;
        if (!(webSecurityManager.getSessionManager() instanceof DefaultSessionManager)) {
            return result;
        }
        DefaultSessionManager sessionManager = (DefaultSessionManager) webSecurityManager.getSessionManager();
        SessionDAO sessionDAO = sessionManager.getSessionDAO();
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();

        long now = System.currentTimeMillis();
        for (Session session : activeSessions) {
            try {
                Object principals = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                Boolean authenticated = (Boolean) session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
                if (!(principals instanceof PrincipalCollection) || !Boolean.TRUE.equals(authenticated)) {
                    continue;
                }
                Object principal = ((PrincipalCollection) principals).getPrimaryPrincipal();
                if (!(principal instanceof User)) {
                    continue;
                }
                if (session.getLastAccessTime() == null
                        || now - session.getLastAccessTime().getTime() > session.getTimeout()) {
                    continue;
                }
                User user = (User) principal;
                ActiveSessionTO to = new ActiveSessionTO();
                to.setSessionId(String.valueOf(session.getId()));
                to.setUserEmail(user.getEmail());
                to.setUserName(user.getFirstName() + " " + user.getLastName());
                to.setHost(session.getHost());
                to.setStartTimestamp(session.getStartTimestamp());
                to.setLastAccessTime(session.getLastAccessTime());
                result.add(to);
            } catch (Exception e) {
                logger.warn("Skipping unreadable session: " + e.getMessage());
            }
        }
        return result;
    }
}
