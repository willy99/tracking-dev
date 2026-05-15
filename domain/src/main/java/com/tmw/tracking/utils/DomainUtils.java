package com.tmw.tracking.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.tmw.tracking.entity.User;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DomainUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DATE_FORMAT_WITH_TZ = "yyyy-MM-dd'T'HH:mm:ss.S";

    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");

    public static final List<String> MUST_BE_SELECTED_DIVISIONS = new ArrayList<String>(){{add("60");}{add("G0");}};

    private final static Logger logger = LoggerFactory.getLogger(DomainUtils.class);

    public static ThreadLocal<Locale> requestLocale = new ThreadLocal<Locale>();



    /**
     * Initialize the Environment
     */
    private static void initializeTrackingEnv() {
        // This allows developers to run this code locally without adding an environment variable
        if (StringUtils.isEmpty(System.getProperty("tracking.env"))) {
            System.setProperty("tracking.env", "dev");
            logger.warn("Setting tracking.env to "
                    + System.getProperty("tracking.env")
                    + ".  It's recommended to explicitly set this java command line parameter in a startup script/configuration. (-Dtracking.env=local)");
        }

    }

    public static Properties getServerVersionProperties() {
        return getPropertiesHelper("server_version.properties");
    }

    public static Integer getDefaultPageSize() {
        String pageSize = DomainUtils.getProperties().getProperty("page.default.size");
        return (pageSize != null? Integer.valueOf(pageSize):10);
    }

    public static Properties getProperties() {
        DomainUtils.initializeTrackingEnv();
        return getPropertiesHelper("tracking-"
                + System.getProperties().getProperty("tracking.env") + ".properties");
    }

    public static Properties getTrackingConstants() {
        String env = System.getProperties().getProperty("tracking.env").contains("test")?"-tests":"";
        return getPropertiesHelper("tracking_constants"+env + ".properties");
    }

    public static double getConstantDoubleValue(String key) {
        String value = getTrackingConstants().getProperty(key);
        if (value != null) {
            return Double.parseDouble(value);
        }
        throw new RuntimeException("Constant " + key + " is not found!");
    }

    public static int getConstantIntegerValue(String key) {
        String value = getTrackingConstants().getProperty(key);
        if (value != null) {
            return Integer.parseInt(value);
        }
        throw new RuntimeException("Constant " + key + " is not found!");
    }

    public static String getConstantStringValue(String key) {
        String value = getTrackingConstants().getProperty(key);
        if (value != null) {
            return value;        }
        throw new RuntimeException("Constant " + key + " is not found!");
    }



    //TODO check if this really touched everytime!
    private static Properties getPropertiesHelper(String path) {
        final Properties applicationProperties = new Properties();
        try {
            InputStream propertiesInputStream = DomainUtils.class.getClassLoader().getResourceAsStream(path);
            applicationProperties.load(propertiesInputStream);
        } catch (IOException e) {
            logger.error(errorToString(e));
        }
        return applicationProperties;
    }



    public static String errorToString(final Throwable e){
        final Writer writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        return writer.toString();
    }

    public static boolean isLong(final String value){
        try {
            Long.valueOf(value);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean isInteger(final String value) {
        try {
            Integer.valueOf(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static <T> List<T> getAllResult(EntityManager entityManager, Class<T> cls, final String sql) {
        final TypedQuery<T> query = entityManager.createQuery(sql, cls);
        return query.getResultList();
    }

    public static <T> List<T> getLimitResult(EntityManager entityManager, Class<T> cls, final String sql, final Integer start, final Integer count) {
        final TypedQuery<T> query = entityManager.createQuery(sql, cls);
        if(count != null)
            query.setMaxResults(count);
        if(start != null)
            query.setFirstResult(start);
        return query.getResultList();
    }

    public static Integer getRowCount(EntityManager entityManager, Class entity) {
        return getRowCount(entityManager, entity, null);
    }

    public static Integer getRowCount(EntityManager entityManager, Class entity, final String where) {
        final Query query = entityManager.createQuery("select count(*) from " + entity.getName() + (StringUtils.isBlank(where) ? "" : " " + where));
        try {
            return ((Number) query.getSingleResult()).intValue();
        } catch(NoResultException e) {
            return 0;
        }
    }


    public static String getTimeAsString(final Date date){
        if(date == null)return null;
        final Calendar current = Calendar.getInstance();
        current.setTime(new Date());
        Date transformed = DateUtils.setYears(date, current.get(Calendar.YEAR));
        transformed = DateUtils.setMonths(transformed, current.get(Calendar.MONTH));
        transformed = DateUtils.setDays(transformed, current.get(Calendar.DAY_OF_MONTH));
        final SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm");
        //if(store != null && StringUtils.isNotBlank(store.getTimeZone()))
        //  timeDateFormat.setTimeZone(TimeZone.getTimeZone(store.getTimeZone()));
        return timeDateFormat.format(transformed);
    }

    public static User getAuthenticatedUser() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser;
        }
        throw new RuntimeException("Token is invalid. User is not logged.");
    }

    public static User getCurrentUser() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() && subject.getPrincipal() != null) {
            return (User) subject.getPrincipal();
        } else {
            return null;
        }
    }

    public static DatabaseReader getGeoPositionFile(String path) {
        try {
            InputStream database = DomainUtils.class.getClassLoader().getResourceAsStream(path);
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
            return dbReader;
        } catch (IOException e) {
            logger.error(errorToString(e));
        }
        return null;
    }



}
