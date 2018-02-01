package org.reportng;

import tools.correlation.CorrelationFunction;
import tools.correlation.getMatchers;
import tools.mail.MailListener;
import org.apache.velocity.VelocityContext;
import org.resultMail.*;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author 李振7
 * Created Time: 2017/11/24 下午4:46
 */
public class HTMLReporter extends AbstractReporter {
    private static final String FRAMES_PROPERTY = "org.uncommons.reportng.frames";
    private static final String ONLY_FAILURES_PROPERTY = "org.uncommons.reportng.failures-only";
    private static final String TEMPLATES_PATH = "org/uncommons/reportng/templates/html/";
    private static final String INDEX_FILE = "TestReportSummary.html";
    private static final String SUITES_FILE = "suites.html";
    private static final String OVERVIEW_FILE = "overview.html";
    private static final String GROUPS_FILE = "groups.html";
    private static final String RESULTS_FILE = "results.html";
    private static final String OUTPUT_FILE = "output.html";
    private static final String CUSTOM_STYLE_FILE = "custom.css";
    private static final String SUITE_KEY = "suite";
    private static final String SUITES_KEY = "suites";
    private static final String GROUPS_KEY = "groups";
    private static final String RESULT_KEY = "result";
    private static final String FAILED_CONFIG_KEY = "failedConfigurations";
    private static final String SKIPPED_CONFIG_KEY = "skippedConfigurations";
    private static final String FAILED_TESTS_KEY = "failedTests";
    private static final String SKIPPED_TESTS_KEY = "skippedTests";
    private static final String PASSED_TESTS_KEY = "passedTests";
    private static final String ONLY_FAILURES_KEY = "onlyReportFailures";
    private static final String REPORT_DIRECTORY = "html";
    private static final Comparator<ITestNGMethod> METHOD_COMPARATOR = new TestMethodComparator();
    private static final Comparator<ITestResult> RESULT_COMPARATOR = new TestResultComparator();
    private static final Comparator<IClass> CLASS_COMPARATOR = new TestClassComparator();
    private String caseName = "";
    private String methodName = "";
    private String failReason = "";
    private String failMethodName = "";
    public  String mailhtml;
    int allPassedTestCases = 0;
    int allfailedTestCases = 0;
    int allSkippedTestCases = 0;

    public HTMLReporter() {
        super("org/uncommons/reportng/templates/html/");
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectoryName) {
        this.removeEmptyDirectories(new File(outputDirectoryName));
        boolean useFrames = "true".equals(System.getProperty("org.uncommons.reportng.frames", "true"));
        boolean onlyFailures = "true".equals(System.getProperty("org.uncommons.reportng.failures-only", "false"));
        File outputDirectory = new File(outputDirectoryName, "html");
        outputDirectory.mkdirs();

        try {
            if (useFrames) {
                this.createFrameset(outputDirectory);
            }

            this.createOverview(suites, outputDirectory, !useFrames, onlyFailures);
            this.createSuiteList(suites, outputDirectory, onlyFailures);
            this.createGroups(suites, outputDirectory);
            this.createResults(suites, outputDirectory, onlyFailures);
            this.createLog(outputDirectory, onlyFailures);
            this.copyResources(outputDirectory);

            //测试MailContext
            VelocityContext context = new VelocityContext();
            StringBuilder tableContent = new StringBuilder();
            String sulteName = "";
            for (ISuite suite : suites) {
                Map<String, ISuiteResult> suiteResults = suite.getResults();
                int suitePassedCount = 0;
                int suiteFailedCount = 0;
                int suiteSkippedCount = 0;
                for (ISuiteResult suiteResult : suiteResults.values()) {
                    ReporterData data = new ReporterData();
                    ITestContext testContext = suiteResult.getTestContext();
                    // 把数据填入上下文
                    //测试结果汇总信息
                    context.put("overView", data.testContext(testContext));
                    //所有的测试方法
                    //ITestNGMethod[] allTests = testContext.getAllTestMethods();
                    //未执行的测试方法
                    //Collection<ITestNGMethod> excludeTests = testContext.getExcludedMethods();
                    //测试通过的测试方法
                    IResultMap passedTests = testContext.getPassedTests();
                    //测试失败的测试方法
                    IResultMap failedTests = testContext.getFailedTests();
                    //测试跳过的测试方法
                    IResultMap skippedTests = testContext.getSkippedTests();

                    context.put("pass", data.testResults(passedTests, ITestResult.SUCCESS));
                    context.put("fail", data.testResults(failedTests, ITestResult.FAILURE));
                    context.put("skip", data.testResults(skippedTests, ITestResult.FAILURE));

                    suitePassedCount = suitePassedCount + data.testContext(testContext).getPassedTestsSize();
                    suiteFailedCount = suiteFailedCount + data.testContext(testContext).getFailedTestsSize();
                    suiteSkippedCount = suiteSkippedCount + data.testContext(testContext).getSkippedTestsSize();
                    if (suiteFailedCount>0) {
                        String AllFileResult = testContext.getFailedTests().getAllResults().toString();
                        if (AllFileResult != null && AllFileResult.length() > 0) {
                            String methodNameRegex = "[\\s\\S]?method=([\\s\\S]+?)\\[pri[\\s\\S]+?[,\\s]+";
                            getMatchers getMatchers = new getMatchers();
                            List<String> s = getMatchers.getMatchers(methodNameRegex, AllFileResult);
                            for (int i = 0; i < s.size(); i++) {
                                //    System.out.println(s.get(i));
                                failMethodName = failMethodName + s.get(i) + " / ";
                            }
                            System.out.println("===========failMethodName==============\n" + failMethodName + "\n===========================");

                            caseName = new CorrelationFunction().getCorrelationStr(AllFileResult, "TestResult name=", " status=");
                            methodName = new CorrelationFunction().getCorrelationStr(AllFileResult, "instance:", "AllProcess@");
                            failReason = new CorrelationFunction().getCorrelationStr(AllFileResult, "output=>>>>>>>>>>>>>>>>>>>>", "<<<<<<<<<<<<<<<<<<<<]]");
                            tableContent = tableContent.append("  <tr>\n" +
                                    "    <td align=\"center\" valign=\"middle\"><font color=\"#0000FF\"><u>" + testContext.getName() + "</u></font></td>\n" +
                                    "    <td bgcolor=\"#00FF99\" align=\"center\" valign=\"middle\">" + data.testContext(testContext).getPassedTestsSize() + "</td>\n" +
                                    "    <td bgcolor=\"#CCFF99\" align=\"center\" valign=\"middle\">" + data.testContext(testContext).getSkippedTestsSize() + "</td>\n" +
                                    "    <td bgcolor=\"#FF3333\" align=\"center\" valign=\"middle\">" + data.testContext(testContext).getFailedTestsSize() + "</td>\n" +
                                    "    <td align=\"center\" valign=\"middle\">" + data.testContext(testContext).getPassPercent() + "</td>\n" +
                                    //    输出失败的用例信息
                                    "    <td>" +
                                    failMethodName +
                                    //"<p><strong>caseName: </strong>" + caseName + "</p>" +
                                    //"<p><strong>methodName: </strong>" + methodName + "</p>" +
                                    //"<p><strong>failReason: </strong>" + failReason +
                                    "</td>\n" +

                                    "  </tr>\n");
                        } else {

                            tableContent = tableContent.append("  <tr>\n" +
                                    "    <td><font color=\"#0000FF\"><u>" + testContext.getName() + "</u></font></td>\n" +
                                    "    <td bgcolor=\"#00FF99\" align=\"center\" valign=\"middle\">" + data.testContext(testContext).getPassedTestsSize() + "</td>\n" +
                                    "    <td bgcolor=\"#CCFF99\" align=\"center\" valign=\"middle\">" + data.testContext(testContext).getSkippedTestsSize() + "</td>\n" +
                                    "    <td bgcolor=\"#FF3333\" align=\"center\" valign=\"middle\">" + data.testContext(testContext).getFailedTestsSize() + "</td>\n" +
                                    "    <td align=\"center\" valign=\"middle\">" + data.testContext(testContext).getPassPercent() + "</td>\n" +
                                    "  </tr>\n");
                        }
                    }

                }

                //获取suiteName
                sulteName = suite.getName();
                allPassedTestCases = allPassedTestCases + suitePassedCount;
                allfailedTestCases = allfailedTestCases + suiteFailedCount;
                allSkippedTestCases = allSkippedTestCases + suiteSkippedCount;
            }
            String allPassRate = NumberFormat.getInstance().format((float) allPassedTestCases /(float)(allPassedTestCases + allfailedTestCases + allSkippedTestCases)*100) + "%";
            String tableSuiteName = "<caption>\n" +
                    sulteName  +"\n" +
                    "  </caption>\n";

            String tabletitle = " <tr>\n" +
                    "    <td width=\"150\" align=\"center\" valign=\"middle\" ><strong>testname</strong></td>\n" +
                    "    <td width=\"70\" align=\"center\" valign=\"middle\" ><strong>Passed</strong></td>\n" +
                    "    <td width=\"70\" align=\"center\" valign=\"middle\" ><strong>Skipped</strong></td>\n" +
                    "    <td width=\"70\" align=\"center\" valign=\"middle\" ><strong>Failed</strong></td>\n" +
                    "    <td width=\"80\" align=\"center\" valign=\"middle\" ><strong>Pass Rate</strong></td>\n" +
                    "    <td width=\"200\" align=\"center\" valign=\"middle\"><strong>Failed Case</strong></td>\n" +
                    "  </tr>\n";


            String lastrow = " <tr>\n" +
                    "    <td bgcolor=\"#CCCCCC\" align=\"center\" valign=\"middle\"><strong>Total</strong></td>\n" +
                    "    <td bgcolor=\"#00FF00\" align=\"center\" valign=\"middle\"><strong>"+ allPassedTestCases +"</strong></td>\n" +
                    "    <td bgcolor=\"#CCFF00\" align=\"center\" valign=\"middle\"><strong>"+ allSkippedTestCases +"</strong></td>\n" +
                    "    <td bgcolor=\"#FF0000\" align=\"center\" valign=\"middle\"><strong>"+ allfailedTestCases +"</strong></td>\n" +
                    "    <td bgcolor=\"#CCCCCC\" align=\"center\" valign=\"middle\"><strong>"+ allPassRate +"</strong></td>\n" +
            //        "    <td bgcolor=\"#999999\" align=\"center\" valign=\"middle\"></td>\n" +
                    "  </tr>\n" +
                    "</table>\n";

            mailhtml = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                    "<title>"+sulteName + "</title>\n" +
                    "</head>\n" + "\n" +
                    "<body>\n" +
                    "<table border=\"1\">\n" +
                    tableSuiteName +
                    tabletitle +
                    tableContent +
                    lastrow +
                    "</table>\n" +
                    "<p><strong>备注：</strong>详细内容见附件</p>\n" +
                    "</body>\n" +
                    "</html>";
            //打包，并发送邮件
            MailListener maillistener = new MailListener();
            maillistener.subject = sulteName;
            maillistener.mailHtml = mailhtml;
            if(allPassedTestCases <(allPassedTestCases + allfailedTestCases + allSkippedTestCases)) {
                maillistener.failMailTest();
            }

        } catch (Exception var8) {
            throw new ReportNGException("Failed generating HTML report.", var8);
        }
    }

    private void createFrameset(File outputDirectory) throws Exception {
        VelocityContext context = this.createContext();
        this.generateFile(new File(outputDirectory, "TestReportSummary.html"), "index.html.vm", context);
    }

    private void createOverview(List<ISuite> suites, File outputDirectory, boolean isIndex, boolean onlyFailures) throws Exception {
        VelocityContext context = this.createContext();
        context.put("suites", suites);
        context.put("onlyReportFailures", onlyFailures);
        this.generateFile(new File(outputDirectory, isIndex ? "TestReportSummary.html" : "overview.html"), "overview.html.vm", context);
    }

    private void createSuiteList(List<ISuite> suites, File outputDirectory, boolean onlyFailures) throws Exception {
        VelocityContext context = this.createContext();
        context.put("suites", suites);
        context.put("onlyReportFailures", onlyFailures);
        this.generateFile(new File(outputDirectory, "suites.html"), "suites.html.vm", context);
    }

    private void createResults(List<ISuite> suites, File outputDirectory, boolean onlyShowFailures) throws Exception {
        int index = 1;

        for(Iterator var5 = suites.iterator(); var5.hasNext(); ++index) {
            ISuite suite = (ISuite)var5.next();
            int index2 = 1;

            for(Iterator var8 = suite.getResults().values().iterator(); var8.hasNext(); ++index2) {
                ISuiteResult result = (ISuiteResult)var8.next();
                boolean failuresExist = result.getTestContext().getFailedTests().size() > 0 || result.getTestContext().getFailedConfigurations().size() > 0;
                if (!onlyShowFailures || failuresExist) {
                    VelocityContext context = this.createContext();
                    context.put("result", result);
                    context.put("failedConfigurations", this.sortByTestClass(result.getTestContext().getFailedConfigurations()));
                    context.put("skippedConfigurations", this.sortByTestClass(result.getTestContext().getSkippedConfigurations()));
                    context.put("failedTests", this.sortByTestClass(result.getTestContext().getFailedTests()));
                    context.put("skippedTests", this.sortByTestClass(result.getTestContext().getSkippedTests()));
                    context.put("passedTests", this.sortByTestClass(result.getTestContext().getPassedTests()));
                    String fileName = String.format("suite%d_test%d_%s", index, index2, "results.html");
                    this.generateFile(new File(outputDirectory, fileName), "results.html.vm", context);
                }
            }
        }

    }

    private SortedMap<IClass, List<ITestResult>> sortByTestClass(IResultMap results) {
        SortedMap<IClass, List<ITestResult>> sortedResults = new TreeMap(CLASS_COMPARATOR);

        ITestResult result;
        Object resultsForClass;
        int index;
        for(Iterator var3 = results.getAllResults().iterator(); var3.hasNext(); ((List)resultsForClass).add(index, result)) {
            result = (ITestResult)var3.next();
            resultsForClass = (List)sortedResults.get(result.getTestClass());
            if (resultsForClass == null) {
                resultsForClass = new ArrayList();
                sortedResults.put(result.getTestClass(), (List<ITestResult>) resultsForClass);
            }

            index = Collections.binarySearch((List)resultsForClass, result, RESULT_COMPARATOR);
            if (index < 0) {
                index = Math.abs(index + 1);
            }
        }

        return sortedResults;
    }

    private void createGroups(List<ISuite> suites, File outputDirectory) throws Exception {
        int index = 1;

        for(Iterator var4 = suites.iterator(); var4.hasNext(); ++index) {
            ISuite suite = (ISuite)var4.next();
            SortedMap<String, SortedSet<ITestNGMethod>> groups = this.sortGroups(suite.getMethodsByGroups());
            if (!groups.isEmpty()) {
                VelocityContext context = this.createContext();
                context.put("suite", suite);
                context.put("groups", groups);
                String fileName = String.format("suite%d_%s", index, "groups.html");
                this.generateFile(new File(outputDirectory, fileName), "groups.html.vm", context);
            }
        }

    }

    private void createLog(File outputDirectory, boolean onlyFailures) throws Exception {
        if (!Reporter.getOutput().isEmpty()) {
            VelocityContext context = this.createContext();
            context.put("onlyReportFailures", onlyFailures);
            this.generateFile(new File(outputDirectory, "output.html"), "output.html.vm", context);
        }

    }

    private SortedMap<String, SortedSet<ITestNGMethod>> sortGroups(Map<String, Collection<ITestNGMethod>> groups) {
        SortedMap<String, SortedSet<ITestNGMethod>> sortedGroups = new TreeMap();
        Iterator var3 = groups.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, Collection<ITestNGMethod>> entry = (Map.Entry)var3.next();
            SortedSet<ITestNGMethod> methods = new TreeSet(METHOD_COMPARATOR);
            methods.addAll((Collection)entry.getValue());
            sortedGroups.put(entry.getKey(), methods);
        }

        return sortedGroups;
    }

    private void copyResources(File outputDirectory) throws IOException {
        this.copyClasspathResource(outputDirectory, "reportng.css", "reportng.css");
        this.copyClasspathResource(outputDirectory, "reportng.js", "reportng.js");
        File customStylesheet = META.getStylesheetPath();
        if (customStylesheet != null) {
            if (customStylesheet.exists()) {
                this.copyFile(outputDirectory, customStylesheet, "custom.css");
            } else {
                InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(customStylesheet.getPath());
                if (stream != null) {
                    this.copyStream(outputDirectory, stream, "custom.css");
                }
            }
        }
    }
}
