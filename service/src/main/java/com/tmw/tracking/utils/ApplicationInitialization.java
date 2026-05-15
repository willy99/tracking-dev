package com.tmw.tracking.utils;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.net.URL;

/**
 * Created by pzhelnov on 8/11/2016.
 */
@Singleton
public class ApplicationInitialization {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationInitialization.class);
    private static final String DEV = "dev";
    private static final String LOG4J = "log4j";
    private static final String XML = ".xml";
    public static boolean LUCENE_INDEXED = false;

    public ApplicationInitialization() {
    }

    @PostConstruct
    public void contextInitialized() {
        //logger init
        String env = System.getProperties().getProperty("tracking.env");
        if (env == null) {
            env = DEV;
        }
        String log4JPropertyFile = LOG4J + "-" + env + XML;
        URL resUrl = getClass().getClassLoader().getResource(log4JPropertyFile);
        PropertyConfigurator.configure(resUrl);
        DOMConfigurator.configure(resUrl);
    }

}
