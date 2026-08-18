package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.domain.MonitoringType;
import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.domain.TestResult;
import com.tmw.tracking.service.MonitoringService;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.aop.MethodCall;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;

import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Path("/monitoring")
@Singleton
public class MonitoringController extends BaseController {

    private final String serverVersion;
    private final MonitoringService monitoringLogic;
    private final EntityManagerProvider entityManagerProvider;

    @Inject
    public MonitoringController(@Named("server.version") final String serverVersion,
                                final MonitoringService monitoringLogic,
                                final EntityManagerProvider entityManagerProvider) {
        this.serverVersion = serverVersion;
        this.monitoringLogic = monitoringLogic;
        this.entityManagerProvider = entityManagerProvider;
    }

    @POST
    @Path("/evictCache")
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.COMPANY_MANAGEMENT)
    public Response evictCache() {
        EntityManagerFactory emf = entityManagerProvider.getEntityManager().getEntityManagerFactory();
        if (emf != null && emf.isOpen()) {
            emf.getCache().evictAll();
        }
        return Response.ok("{\"status\":\"ok\"}").build();
    }



    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/")
    public Viewable getInfo() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        vars.put("version", serverVersion);
        try {
            vars.put("cloudInfo", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return new Viewable("/monitoring/monitoring", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/tests")
    public Viewable getTests() {
        MonitoringType[] values = MonitoringType.values();
        Map<String, String> testMap = new LinkedHashMap<String, String>();
        for(MonitoringType value : values) {
            testMap.put(value.name(), value.getDesc());
        }
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("environment", environment);
        vars.put("tests", testMap);
        vars.put("version", serverVersion);
        return new Viewable("/monitoring/monitoring", vars);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/start")
    public String startTest(@QueryParam("testId") String testId) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if(!MonitoringType.isExist(testId)) {
            vars.put("errorMessage", "Test not found: " + testId);
        } else {
            List<TestResult> testResults = monitoringLogic.startTests(Arrays.asList(MonitoringType.valueOf(testId)));
            for (TestResult testResult: testResults) {
                testResult.setTestResult(testResult.getTestResult().replaceAll(System.getProperty("line.separator"),"<br>"));
            }
            vars.put("results", testResults);
        }
        return Utils.toJson(vars);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/lastMonitoring")
    public String getLastMonitoring() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        List<TestResult> testResults = monitoringLogic.getLastMonitoring();
        vars.put("results", testResults);
        return Utils.toJson(vars);
    }




}
