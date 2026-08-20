package com.tmw.tracking.utils;

import javax.inject.Singleton;

/**
 * Created by pzhelnov on 8/11/2016.
 *
 * Logging used to be bootstrapped here by hand-picking a log4j-&lt;env&gt;.xml file. Logback
 * (which replaced log4j) auto-configures itself from logback.xml on the classpath, so that
 * step is gone — this class now only holds the Lucene-indexing flag {@link EntityManagerProvider}
 * checks.
 */
@Singleton
public class ApplicationInitialization {

    public static boolean LUCENE_INDEXED = false;

}
