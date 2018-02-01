package org.reportng;

import org.testng.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author 李振7
 * Created Time: 2017/11/24 下午4:49
 */
public class ReportNGUtils {
    private static final NumberFormat DURATION_FORMAT = new DecimalFormat("#0.000");
    private static final NumberFormat PERCENTAGE_FORMAT = new DecimalFormat("#0.00%");

    public ReportNGUtils() {
    }

    /**
        获取响应时间
     */
    public long getDuration(ITestContext context) {
        long duration = this.getDuration(context.getPassedConfigurations().getAllResults());
        duration += this.getDuration(context.getPassedTests().getAllResults());
        duration += this.getDuration(context.getSkippedConfigurations().getAllResults());
        duration += this.getDuration(context.getSkippedTests().getAllResults());
        duration += this.getDuration(context.getFailedConfigurations().getAllResults());
        duration += this.getDuration(context.getFailedTests().getAllResults());
        return duration;
    }

    private long getDuration(Set<ITestResult> results) {
        long duration = 0L;

        ITestResult result;
        for(Iterator var4 = results.iterator(); var4.hasNext(); duration += result.getEndMillis() - result.getStartMillis()) {
            result = (ITestResult)var4.next();
        }

        return duration;
    }

    public String formatDuration(long startMillis, long endMillis) {
        long elapsed = endMillis - startMillis;
        return this.formatDuration(elapsed);
    }

    public String formatDuration(long elapsed) {
        double seconds = (double)elapsed / 1000.0D;
        return DURATION_FORMAT.format(seconds);
    }

    public List<Throwable> getCauses(Throwable t) {
        List<Throwable> causes = new LinkedList();
        Throwable next = t;

        while(next.getCause() != null) {
            next = next.getCause();
            causes.add(next);
        }

        return causes;
    }

    public List<String> getTestOutput(ITestResult result) {
        return Reporter.getOutput(result);
    }

    public List<String> getAllOutput() {
        return Reporter.getOutput();
    }

    public boolean hasArguments(ITestResult result) {
        return result.getParameters().length > 0;
    }

    public String getArguments(ITestResult result) {
        Object[] arguments = result.getParameters();
        List<String> argumentStrings = new ArrayList(arguments.length);
        Object[] var4 = arguments;
        int var5 = arguments.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Object argument = var4[var6];
            argumentStrings.add(this.renderArgument(argument));
        }

        return this.commaSeparate(argumentStrings);
    }

    private String renderArgument(Object argument) {
        if (argument == null) {
            return "null";
        } else if (argument instanceof String) {
            return "\"" + argument + "\"";
        } else {
            return argument instanceof Character ? "'" + argument + "'" : argument.toString();
        }
    }

    public boolean hasDependentGroups(ITestResult result) {
        return result.getMethod().getGroupsDependedUpon().length > 0;
    }

    public String getDependentGroups(ITestResult result) {
        String[] groups = result.getMethod().getGroupsDependedUpon();
        return this.commaSeparate(Arrays.asList(groups));
    }

    public boolean hasDependentMethods(ITestResult result) {
        return result.getMethod().getMethodsDependedUpon().length > 0;
    }

    public String getDependentMethods(ITestResult result) {
        String[] methods = result.getMethod().getMethodsDependedUpon();
        return this.commaSeparate(Arrays.asList(methods));
    }

    public boolean hasSkipException(ITestResult result) {
        return result.getThrowable() instanceof SkipException;
    }

    public String getSkipExceptionMessage(ITestResult result) {
        return this.hasSkipException(result) ? result.getThrowable().getMessage() : "";
    }

    public boolean hasGroups(ISuite suite) {
        return !suite.getMethodsByGroups().isEmpty();
    }

    private String commaSeparate(Collection<String> strings) {
        StringBuilder buffer = new StringBuilder();
        Iterator iterator = strings.iterator();

        while(iterator.hasNext()) {
            String string = (String)iterator.next();
            buffer.append(string);
            if (iterator.hasNext()) {
                buffer.append(", ");
            }
        }

        return buffer.toString();
    }

    public String escapeString(String s) {
        if (s == null) {
            return null;
        } else {
            StringBuilder buffer = new StringBuilder();

            for(int i = 0; i < s.length(); ++i) {
                buffer.append(this.escapeChar(s.charAt(i)));
            }

            return buffer.toString();
        }
    }

    private String escapeChar(char character) {
        switch(character) {
            case '"':
                return "&quot;";
            case '&':
                return "&amp;";
            case '\'':
                return "&apos;";
            case '<':
                return "&lt;";
            case '>':
                return "&gt;";
            default:
                return String.valueOf(character);
        }
    }

    public String escapeHTMLString(String s) {
        if (s == null) {
            return null;
        } else {
            StringBuilder buffer = new StringBuilder();

            for(int i = 0; i < s.length(); ++i) {
                char ch = s.charAt(i);
                switch(ch) {
                    case '\n':
                        buffer.append("<br/>\n");
                        break;
                    case ' ':
                        char nextCh = i + 1 < s.length() ? s.charAt(i + 1) : 0;
                        buffer.append(nextCh == ' ' ? "&nbsp;" : " ");
                        break;
                    default:
                        buffer.append(this.escapeChar(ch));
                }
            }

            return buffer.toString();
        }
    }

    public String stripThreadName(String threadId) {
        if (threadId == null) {
            return null;
        } else {
            int index = threadId.lastIndexOf(64);
            return index >= 0 ? threadId.substring(0, index) : threadId;
        }
    }

    public long getStartTime(List<IInvokedMethod> methods) {
        long startTime = System.currentTimeMillis();

        IInvokedMethod method;
        for(Iterator var4 = methods.iterator(); var4.hasNext(); startTime = Math.min(startTime, method.getDate())) {
            method = (IInvokedMethod)var4.next();
        }

        return startTime;
    }

    public long getEndTime(ISuite suite, IInvokedMethod method, List<IInvokedMethod> methods) {
        boolean found = false;
        Iterator var5 = methods.iterator();

        while(var5.hasNext()) {
            IInvokedMethod m = (IInvokedMethod)var5.next();
            if (m == method) {
                found = true;
            } else if (found && m.getTestMethod().getId().equals(method.getTestMethod().getId())) {
                return m.getDate();
            }
        }

        return this.getEndTime(suite, method);
    }

    private long getEndTime(ISuite suite, IInvokedMethod method) {
        Iterator var3 = suite.getResults().entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, ISuiteResult> entry = (Map.Entry)var3.next();
            ITestContext testContext = ((ISuiteResult)entry.getValue()).getTestContext();
            ITestNGMethod[] var6 = testContext.getAllTestMethods();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                ITestNGMethod m = var6[var8];
                if (method == m) {
                    return testContext.getEndDate().getTime();
                }
            }

            Iterator var10 = testContext.getPassedConfigurations().getAllMethods().iterator();

            ITestNGMethod m;
            while(var10.hasNext()) {
                m = (ITestNGMethod)var10.next();
                if (method == m) {
                    return testContext.getEndDate().getTime();
                }
            }

            var10 = testContext.getFailedConfigurations().getAllMethods().iterator();

            while(var10.hasNext()) {
                m = (ITestNGMethod)var10.next();
                if (method == m) {
                    return testContext.getEndDate().getTime();
                }
            }
        }

        throw new IllegalStateException("Could not find matching end time.");
    }

    public String formatPercentage(int numerator, int denominator) {
        return PERCENTAGE_FORMAT.format((double)numerator / (double)denominator);
    }
}
