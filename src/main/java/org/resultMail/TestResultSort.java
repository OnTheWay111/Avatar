package org.resultMail;

import org.testng.ITestResult;

/**
 * @author 李振7
 * Created Time: 2017/11/28 下午2:51
 */
public class TestResultSort implements Comparable<ITestResult> {
    private Long order;
    @Override
    public int compareTo(ITestResult arg0) {
        // TODO Auto-generated method stub
        return this.order.compareTo( arg0.getStartMillis());//按test开始时间排序
    }

}
