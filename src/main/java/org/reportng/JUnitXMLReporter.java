package org.reportng;

import org.apache.velocity.VelocityContext;
import org.testng.IClass;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import org.uncommons.reportng.AbstractReporter;
import org.uncommons.reportng.ReportNGException;

import java.io.File;
import java.util.*;

/**
 * @author 李振7
 * Created Time: 2017/11/24 下午4:48
 */
public class JUnitXMLReporter extends AbstractReporter {
    private static final String RESULTS_KEY = "results";
    private static final String TEMPLATES_PATH = "org/uncommons/reportng/templates/xml/";
    private static final String RESULTS_FILE = "results.xml";
    private static final String REPORT_DIRECTORY = "xml";

    public JUnitXMLReporter() {
        super("org/uncommons/reportng/templates/xml/");
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectoryName) {
        this.removeEmptyDirectories(new File(outputDirectoryName));
        File outputDirectory = new File(outputDirectoryName, "xml");
        outputDirectory.mkdirs();
        Collection<TestClassResults> flattenedResults = this.flattenResults(suites);
        Iterator var6 = flattenedResults.iterator();

        while(var6.hasNext()) {
            TestClassResults results = (TestClassResults)var6.next();
            VelocityContext context = this.createContext();
            context.put("results", results);

            try {
                this.generateFile(new File(outputDirectory, results.getTestClass().getName() + '_' + "results.xml"), "results.xml.vm", context);
            } catch (Exception var10) {
                throw new ReportNGException("Failed generating JUnit XML report.", var10);
            }
        }

    }

    private Collection<TestClassResults> flattenResults(List<ISuite> suites) {
        Map<IClass, TestClassResults> flattenedResults = new HashMap<IClass, TestClassResults>(100);
        Iterator var3 = suites.iterator();

        while(var3.hasNext()) {
            ISuite suite = (ISuite)var3.next();
            Iterator var5 = suite.getResults().values().iterator();

            while(var5.hasNext()) {
                ISuiteResult suiteResult = (ISuiteResult)var5.next();
                this.organiseByClass(suiteResult.getTestContext().getFailedConfigurations().getAllResults(), flattenedResults);
                this.organiseByClass(suiteResult.getTestContext().getSkippedConfigurations().getAllResults(), flattenedResults);
                this.organiseByClass(suiteResult.getTestContext().getFailedTests().getAllResults(), flattenedResults);
                this.organiseByClass(suiteResult.getTestContext().getSkippedTests().getAllResults(), flattenedResults);
                this.organiseByClass(suiteResult.getTestContext().getPassedTests().getAllResults(), flattenedResults);
            }
        }

        return flattenedResults.values();
    }

    private void organiseByClass(Set<ITestResult> testResults, Map<IClass, TestClassResults> flattenedResults) {
        Iterator var3 = testResults.iterator();

        while(var3.hasNext()) {
            ITestResult testResult = (ITestResult)var3.next();
            this.getResultsForClass(flattenedResults, testResult).addResult(testResult);
        }

    }

    private TestClassResults getResultsForClass(Map<IClass, TestClassResults> flattenedResults, ITestResult testResult) {
        TestClassResults resultsForClass = (TestClassResults)flattenedResults.get(testResult.getTestClass());
        if (resultsForClass == null) {
            resultsForClass = new TestClassResults(testResult.getTestClass());
            flattenedResults.put(testResult.getTestClass(), resultsForClass);
        }

        return resultsForClass;
    }

    public static final class TestClassResults {
        private final IClass testClass;
        private final Collection<ITestResult> failedTests;
        private final Collection<ITestResult> skippedTests;
        private final Collection<ITestResult> passedTests;
        private long duration;

        private TestClassResults(IClass testClass) {
            this.failedTests = new LinkedList();
            this.skippedTests = new LinkedList();
            this.passedTests = new LinkedList();
            this.duration = 0L;
            this.testClass = testClass;
        }

        public IClass getTestClass() {
            return this.testClass;
        }

        void addResult(ITestResult result) {
            switch(result.getStatus()) {
                case 1:
                    this.passedTests.add(result);
                    break;
                case 3:
                    if (AbstractReporter.META.allowSkippedTestsInXML()) {
                        this.skippedTests.add(result);
                        break;
                    }
                case 2:
                case 4:
                    this.failedTests.add(result);
            }

            this.duration += result.getEndMillis() - result.getStartMillis();
        }

        public Collection<ITestResult> getFailedTests() {
            return this.failedTests;
        }

        public Collection<ITestResult> getSkippedTests() {
            return this.skippedTests;
        }

        public Collection<ITestResult> getPassedTests() {
            return this.passedTests;
        }

        public long getDuration() {
            return this.duration;
        }
    }
}
