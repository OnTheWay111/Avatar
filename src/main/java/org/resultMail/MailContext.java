package org.resultMail;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author 李振7
 * Created Time: 2017/11/28 下午3:12
 */
public class MailContext implements IReporter {
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        // TODO Auto-generated method stub
        try {
            // 初始化并取得Velocity引擎
            VelocityEngine ve = new VelocityEngine();
            Properties p = new Properties();
            //虽然不懂为什么这样设置,但结果是好的.可以用了
            p.setProperty("resource.loader", "class");
            p.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            ve.init(p);
//            Template t = ve.getTemplate("com/reporter/VMmodel/overview.vm");
            VelocityContext context = new VelocityContext();

            for (ISuite suite : suites) {
                Map<String, ISuiteResult> suiteResults = suite.getResults();
                for (ISuiteResult suiteResult : suiteResults.values()) {
                    ReporterData data = new ReporterData();
                    ITestContext testContext = suiteResult.getTestContext();
                    // 把数据填入上下文
                    context.put("overView", data.testContext(testContext));//测试结果汇总信息
                    //ITestNGMethod[] allTests = testContext.getAllTestMethods();//所有的测试方法
                    //Collection<ITestNGMethod> excludeTests = testContext.getExcludedMethods();//未执行的测试方法
                    IResultMap passedTests = testContext.getPassedTests();//测试通过的测试方法
                    IResultMap failedTests = testContext.getFailedTests();//测试失败的测试方法
                    IResultMap skippedTests = testContext.getSkippedTests();//测试跳过的测试方法

                    context.put("pass", data.testResults(passedTests, ITestResult.SUCCESS));
                    context.put("fail", data.testResults(failedTests, ITestResult.FAILURE));
                    context.put("skip", data.testResults(skippedTests, ITestResult.FAILURE));

                    System.out.println(context.get("overView"));


                }
            }
            // 输出流
            OutputStream out=new FileOutputStream("report.html");
            Writer writer = new BufferedWriter(new OutputStreamWriter(out,"utf-8"));//解决乱码问题
            // 转换输出
//            t.merge(context, writer);
            //System.out.println(writer.toString());
            writer.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
