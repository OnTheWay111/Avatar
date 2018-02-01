package org.resultMail;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author 李振7
 * Created Time: 2017/11/28 下午2:50
 */
public class ReportUnits {
    private static final NumberFormat DURATION_FORMAT = new DecimalFormat("#0.000");
    private static final NumberFormat PERCENTAGE_FORMAT = new DecimalFormat("#0.00%");
    /**
     *测试消耗时长
     *return 秒,保留3位小数
     */
    public String getTestDuration(ITestContext context){
        long duration;
        duration=context.getEndDate().getTime()-context.getStartDate().getTime();
        return formatDuration(duration);
    }

    public String formatDuration(long elapsed)
    {
        double seconds = (double) elapsed / 1000;
        return DURATION_FORMAT.format(seconds);
    }
    /**
     *测试通过率
     *return 2.22%,保留2位小数
     */
    public String formatPercentage(int numerator, int denominator)
    {
        return PERCENTAGE_FORMAT.format(numerator / (double) denominator);
    }

    /**
     * 获取方法参数，以逗号分隔
     * @param result
     * @return
     */
    public String getParams(ITestResult result){
        Object[] params = result.getParameters();
        List<String> list = new ArrayList<String>(params.length);
        for (Object o:params){
            list.add(renderArgument(o));
        }
        return  commaSeparate(list);
    }
    /**
     * 获取依赖的方法
     * @param result
     * @return
     */
    public String getDependMethods(ITestResult result){
        String[] methods=result.getMethod().getMethodsDependedUpon();
        return commaSeparate(Arrays.asList(methods));
    }
    /**
     * 堆栈轨迹，暂不确定怎么做，放着先
     * @param throwable
     * @return
     */
    public String getCause(Throwable throwable){
        StackTraceElement[] stackTrace=throwable.getStackTrace(); //堆栈轨迹
        List<String> list = new ArrayList<String>(stackTrace.length);
        for (Object o:stackTrace){
            list.add(renderArgument(o));
        }
        return  commaSeparate(list);
    }
    /**
     * 获取全部日志输出信息
     * @return
     */
    public List<String> getAllOutput(){
        return Reporter.getOutput();
    }

    /**
     * 按testresult获取日志输出信息
     * @param result
     * @return
     */
    public List<String> getTestOutput(ITestResult result){
        return Reporter.getOutput(result);
    }


    /*将object 转换为String*/
    private String renderArgument(Object argument)
    {
        if (argument == null)
        {
            return "null";
        }
        else if (argument instanceof String)
        {
            return "\"" + argument + "\"";
        }
        else if (argument instanceof Character)
        {
            return "\'" + argument + "\'";
        }
        else
        {
            return argument.toString();
        }
    }
    /*将集合转换为以逗号分隔的字符串*/
    private String commaSeparate(Collection<String> strings)
    {
        StringBuilder buffer = new StringBuilder();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext())
        {
            String string = iterator.next();
            buffer.append(string);
            if (iterator.hasNext())
            {
                buffer.append(", ");
            }
        }
        return buffer.toString();
    }
}
