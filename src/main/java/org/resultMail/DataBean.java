package org.resultMail;

import org.testng.ITestNGMethod;

import java.util.Collection;
import java.util.List;

/**
 * @author 李振7
 * Created Time: 2017/11/28 下午2:49
 */
public class DataBean {
    private int excludeTestsSize; //未执行的test数量
    private int passedTestsSize; //测试通过的数量
    private int failedTestsSize; //测试失败的数量
    private int skippedTestsSize; //测试跳过的数量
    private int allTestsSize; //全部执行的测试的数量
    private ITestNGMethod[] allTestsMethod; //全部执行的测试方法
    private Collection<ITestNGMethod> excludeTestsMethod; //未执行的测试方法
    private String testsTime; //测试耗时
    private String passPercent; //测试通过率
    private String testName; //测试方法名
    private String className; //测试类名
    private String duration; //单个测试周期
    private String params; //测试用参数
    private String description; //测试描述
    private List<String> output; //Reporter Output
    private String dependMethod; //测试依赖方法
    private Throwable throwable; //测试异常原因
    private StackTraceElement[] stackTrace; // 异常堆栈信息

    public int getExcludeTestsSize() {
        return excludeTestsSize;
    }

    public void setExcludeTestsSize(int excludeTestsSize) {
        this.excludeTestsSize = excludeTestsSize;
    }

    public int getPassedTestsSize() {
        return passedTestsSize;
    }

    public void setPassedTestsSize(int passedTestsSize) {
        this.passedTestsSize = passedTestsSize;
    }

    public int getFailedTestsSize() {
        return failedTestsSize;
    }

    public void setFailedTestsSize(int failedTestsSize) {
        this.failedTestsSize = failedTestsSize;
    }

    public int getSkippedTestsSize() {
        return skippedTestsSize;
    }

    public void setSkippedTestsSize(int skippedTestsSize) {
        this.skippedTestsSize = skippedTestsSize;
    }

    public int getAllTestsSize() {
        return allTestsSize;
    }

    public void setAllTestsSize(int allTestsSize) {
        this.allTestsSize = allTestsSize;
    }

    public String getPassPercent() {
        return passPercent;
    }

    public void setPassPercent(String passPercent) {
        this.passPercent = passPercent;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }

    public String getDependMethod() {
        return dependMethod;
    }

    public void setDependMethod(String dependMethod) {
        this.dependMethod = dependMethod;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable2) {
        this.throwable = throwable2;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void setTestsTime(String testsTime) {
        this.testsTime = testsTime;
    }

    public String getTestsTime() {
        return testsTime;
    }

    public void setAllTestsMethod(ITestNGMethod[] allTestsMethod) {
        this.allTestsMethod = allTestsMethod;
    }

    public ITestNGMethod[] getAllTestsMethod() {
        return allTestsMethod;
    }

    public void setExcludeTestsMethod(Collection<ITestNGMethod> excludeTestsMethod) {
        this.excludeTestsMethod = excludeTestsMethod;
    }

    public Collection<ITestNGMethod> getExcludeTestsMethod() {
        return excludeTestsMethod;
    }
}
