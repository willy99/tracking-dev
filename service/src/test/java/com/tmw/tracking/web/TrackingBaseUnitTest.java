package com.tmw.tracking.web;

import com.google.inject.Injector;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.tmw.tracking.dao.*;
import com.tmw.tracking.dao.impl.*;
import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.domain.LoginResponse;
import com.tmw.tracking.entity.*;
import com.tmw.tracking.entity.enums.ContainerGroup;
import com.tmw.tracking.entity.enums.OrderStatus;
import com.tmw.tracking.entity.enums.Trend;
import com.tmw.tracking.filter.TrackingAuthenticationToken;
import com.tmw.tracking.filter.TrackingCredentialsMatcher;
import com.tmw.tracking.filter.TrackingSecurityRealm;
import com.tmw.tracking.utils.DomainUtils;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import com.tmw.tracking.web.service.auth.AuthenticationResource;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import java.util.*;

public abstract class TrackingBaseUnitTest {
    private static final Logger logger = LoggerFactory.getLogger(TrackingBaseUnitTest.class);
    private static final String DATA_XML = "test-data.xml";
    public static final String DEFAULT_USER_ID = "test@example.com";
    public static final String DEFAULT_USER_PASS = "Admin123";
    protected static List<RootEntity> entitiesToBeRemoved = new ArrayList<RootEntity>();
    protected static Injector injector;
    private TerminalDao terminalDao = injector.getInstance(TerminalDaoImpl.class);;
    private UserDao userDao = injector.getInstance(UserDaoImpl.class);;
    private TrackingLineDao trackingLineDao = injector.getInstance(TrackingLineDaoImpl.class);;
    private DriverDao driverDao = injector.getInstance(DriverDaoImpl.class);
    private ContainerTypeDao containerTypeDao = injector.getInstance(ContainerTypeDaoImpl.class);
    private EntityManager entityManager = injector.getInstance(EntityManager.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        if (StringUtils.isEmpty(System.getProperty("tracking.env"))) {
            System.setProperty("tracking.env", "tests");
            addJNDI();
            logger.warn("Setting tracking.env to " + System.getProperty("tracking.env")
                    + ".  It's recommended to explicitly set this java command line parameter in a startup script/configuration. (-Dtracking.env=local)");
        }

        if(injector == null) {
            injector = GuiceInstanceHolderTest.getInjector();
        }
    }

    @Before
    public void setUp() throws Exception {
        initDBEnv();
    }

    @After
    public void tearDown() throws Exception {
        final EntityManagerProvider entityManagerProvider = injector.getInstance(EntityManagerProvider.class);
        entityManagerProvider.close();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        final EntityManagerProvider entityManagerProvider = injector.getInstance(EntityManagerProvider.class);
        entityManagerProvider.create();
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        entityManager.getTransaction().begin();
        ListIterator<RootEntity> li = entitiesToBeRemoved.listIterator(entitiesToBeRemoved.size());
        while(li.hasPrevious()) {
            RootEntity entity = li.previous();
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        }
        entityManager.getTransaction().commit();

        entityManagerProvider.close();
    }

    public static void initDBEnv() throws Exception {
        final Properties properties = DomainUtils.getProperties();

        final EntityManagerProvider entityManagerProvider = injector.getInstance(EntityManagerProvider.class);
        final AuthenticationResource authenticationResource = injector.getInstance(AuthenticationResource.class);

        final TrackingSecurityRealm trackingSecurityRealm = new TestTrackingSecurityRealm();
        final TrackingCredentialsMatcher matcher = new TrackingCredentialsMatcher();
        matcher.setHashAlgorithmName("sha-256");
        trackingSecurityRealm.setCredentialsMatcher(matcher);
        SecurityUtils.setSecurityManager(new DefaultSecurityManager(trackingSecurityRealm));

        entityManagerProvider.create();
        entityManagerProvider.applyGlobalFilters();

        LoginRequest loginRequest = createLoginRequest(properties);
        loginUser(authenticationResource, loginRequest);


    }

    private static LoginRequest createLoginRequest(Properties properties) {
        LoginRequest loginRequest = new LoginRequest();
        String userId = properties.getProperty("auth.user.id");
        String userPass = properties.getProperty("auth.user.password");
        loginRequest.setUserId(userId!=null?userId:DEFAULT_USER_ID);
        loginRequest.setPassword(userPass!=null?userPass:DEFAULT_USER_PASS);
        return loginRequest;
    }

    private static Subject loginUser(final AuthenticationResource authenticationResource, LoginRequest loginRequest ) throws Exception {
        int count = 0;
        while (count < 5) {
            try {
                ensureUserIsLoggedOut();
                Subject subject = getSubject();
                LoginResponse response = authenticationResource.login(loginRequest);

                logger.debug("Login with userid:" + loginRequest.getUserId());
                subject.login(new TrackingAuthenticationToken(response.getToken()));
                return subject;
            } catch (Exception e) {
                logger.error("Error Login with token:" + e.getMessage());
                Thread.sleep(1000L);
                count ++;
            }
        }

        return getSubject();
    }

    private static Subject getSubject() {
        Subject currentUser = ThreadContext.getSubject();// SecurityUtils.getSubject();

        if (currentUser == null) {
            currentUser = SecurityUtils.getSubject();
        }

        return currentUser;
    }

    private static void ensureUserIsLoggedOut() {
        try {
            // Get the user if one is logged in.
            Subject currentUser = getSubject();
            if (currentUser == null)
                return;

            // Log the user out and kill their session if possible.
            currentUser.logout();
            org.apache.shiro.session.Session session = currentUser.getSession(false);
            if (session == null)
                return;

            session.stop();
        }
        catch (Exception e) {
            logger.error("Logout error:"+e.getMessage());
        }
    }


    protected User createUser(){
        User user = new User();
        user.setEmail(stringGenerator());
        user.setActive(true);
        user.setFirstName(stringGenerator());
        user.setLastName(stringGenerator());
        user.setPhone(stringGenerator());
        user.setPassword(stringGenerator());
        user.setLocale(new Locale("ukr"));
        entitiesToBeRemoved.add(user);
        return userDao.create(user);
    }


    protected TrackingLine createTrackingLine(){
        TrackingLine line = new TrackingLine();
        line.setName(stringGenerator());
        entitiesToBeRemoved.add(line);
        return trackingLineDao.create(line);
    }


    protected Terminal createTerminal(){
        Terminal terminal =  new Terminal();
        terminal.setAddress(stringGenerator());
        terminal.setCity(stringGenerator());
        terminal.setCountry(stringGenerator());
        terminal.setName(stringGenerator());
        entitiesToBeRemoved.add(terminal);
        return terminalDao.create(terminal);
    }


    protected Driver createDriver(){
        Driver driver = new Driver();
        driver.setFirstName(stringGenerator());
        driver.setLastName(stringGenerator());
        driver.setLength(5);
        driver.setMobile(stringGenerator());
        driver.setTractorNumber(stringGenerator());
        driver.setTrailerNumber(stringGenerator());
        entitiesToBeRemoved.add(driver);
        return driverDao.create(driver);
    }


    protected ContainerType createContainerType(){

        ContainerType containerType = new ContainerType();
        containerType.setContainerGroup(ContainerGroup.DV);
        containerType.setName("20");
        containerType.setLength(5758d);
        containerType.setWidth(2352d);
        containerType.setHeight(2385d);
        containerType.setWorkload(26000.0d);
        containerType.setFreightRate(0d);
        containerType.setMaxWorkload(DomainUtils.getConstantDoubleValue("calculator_parameter_container_max_workload"));
        containerType.setSpecificTonnage(1.273d);
        entitiesToBeRemoved.add(containerType);
        return containerTypeDao.create(containerType);
    }

    protected Order setOrderDetailsAndWorklof(Order order){
        List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
        OrderDetails orderDetails1= new OrderDetails();
        orderDetails1.setDriver(createDriver());
        orderDetails1.setContainerType(createContainerType());
        orderDetails1.setOrder(order);
        orderDetails1.setContainerNumber(stringGenerator());
        orderDetails1.setWeight(25000d);
        orderDetailsList.add(orderDetails1);
        order.setOrderDetails(orderDetailsList);
        List<OrderWorkflow> orderWorkflowList = new ArrayList<OrderWorkflow>();
        OrderWorkflow orderWorkflow = new OrderWorkflow();
        orderWorkflow.setDealDate(new Date());
        orderWorkflow.setStatus(OrderStatus.ACTIVE);
        orderWorkflow.setOrder(order);
        orderWorkflowList.add(orderWorkflow);
        order.setWorkflow(orderWorkflowList);
        return order;
    }

    protected Order createOrder() {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setTrackingLine(createTrackingLine());
        order.setTerminal(createTerminal());
        order.setClient(createUser());
        order.setContainerQty(9);
        order.setOrder1c(stringGenerator());
        order.setTrend(Trend.EXPORT);
        order.setContentName(stringGenerator());
        return order;
    }

     protected String  stringGenerator() {
        String characters = "ABCDEFGHKLMYIJ";
        Random random = new Random();
        char[] text = new char[5];
        for (int i = 0; i < text.length; i++){
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }


    // ========================================================================
    private static  void addJNDI(){
        try {
            // Create initial context
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES,
                    "org.apache.naming");
            InitialContext ic = new InitialContext();

            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/jdbc");

            // Construct DataSource
            final Properties properties = DomainUtils.getProperties();
            final MysqlDataSource ds = new MysqlDataSource();
            String dbUrl = properties.get("hibernate.connection.host") + "/"
                    + ":" + properties.get("hibernate.connection.port") +"/" + properties.get("hibernate.connection.db") + "?useSSL=false";
            ds.setURL(dbUrl);
            ds.setUser((String)properties.get("hibernate.connection.username"));
            ds.setPassword((String)properties.get("hibernate.connection.password"));

            ic.bind("java:comp/env/jdbc/tracking", ds);
        } catch (Exception ex) {
            logger.error(Utils.errorToString(ex));
        }
    }

}
