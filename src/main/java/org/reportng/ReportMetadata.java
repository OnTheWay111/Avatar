package org.reportng;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author 李振7
 * Created Time: 2017/11/24 下午4:48
 */
public final class ReportMetadata {
    static final String PROPERTY_KEY_PREFIX = "org.uncommons.reportng.";
    static final String TITLE_KEY = "org.uncommons.reportng.title";
    static final String DEFAULT_TITLE = "Test Results Report";
    static final String COVERAGE_KEY = "org.uncommons.reportng.coverage-report";
    static final String EXCEPTIONS_KEY = "org.uncommons.reportng.show-expected-exceptions";
    static final String OUTPUT_KEY = "org.uncommons.reportng.escape-output";
    static final String XML_DIALECT_KEY = "org.uncommons.reportng.xml-dialect";
    static final String STYLESHEET_KEY = "org.uncommons.reportng.stylesheet";
    static final String LOCALE_KEY = "org.uncommons.reportng.locale";
    static final String VELOCITY_LOG_KEY = "org.uncommons.reportng.velocity-log";
    final DateFormat DATE_FORMAT = new SimpleDateFormat("EEEE dd MMMM yyyy");
    final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm z");
    final Date reportTime = new Date();

    public ReportMetadata() {
    }

    public String getReportDate() {
        return DATE_FORMAT.format(this.reportTime);
    }

    public String getReportTime() {
        return TIME_FORMAT.format(this.reportTime);
    }

    public String getReportTitle() {
        return System.getProperty("org.uncommons.reportng.title", "Test Results Report");
    }

    public String getCoverageLink() {
        return System.getProperty("org.uncommons.reportng.coverage-report");
    }

    public File getStylesheetPath() {
        String path = System.getProperty("org.uncommons.reportng.stylesheet");
        return path == null ? null : new File(path);
    }

    public boolean shouldShowExpectedExceptions() {
        return "true".equalsIgnoreCase(System.getProperty("org.uncommons.reportng.show-expected-exceptions", "false"));
    }

    public boolean shouldEscapeOutput() {
        return "true".equalsIgnoreCase(System.getProperty("org.uncommons.reportng.escape-output", "true"));
    }

    public boolean allowSkippedTestsInXML() {
        return !"junit".equalsIgnoreCase(System.getProperty("org.uncommons.reportng.xml-dialect", "testng"));
    }

    public boolean shouldGenerateVelocityLog() {
        return "true".equalsIgnoreCase(System.getProperty("org.uncommons.reportng.velocity-log", "false"));
    }

    public String getUser() throws UnknownHostException {
        String user = System.getProperty("user.name");
        String host = InetAddress.getLocalHost().getHostName();
        return user + '@' + host;
    }

    public String getJavaInfo() {
        return String.format("Java %s (%s)", System.getProperty("java.version"), System.getProperty("java.vendor"));
    }

    public String getPlatform() {
        return String.format("%s %s (%s)", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
    }

    public Locale getLocale() {
        String str = "org.uncommons.reportng.locale";
        if (System.getProperties().containsKey(str)) {
            String locale = System.getProperty(str);
            String[] components = locale.split("_", 3);
            switch(components.length) {
                case 1:
                    return new Locale(locale);
                case 2:
                    return new Locale(components[0], components[1]);
                case 3:
                    return new Locale(components[0], components[1], components[2]);
                default:
                    System.err.println("Invalid locale specified: " + locale);
            }
        }

        return Locale.getDefault();
    }
}
