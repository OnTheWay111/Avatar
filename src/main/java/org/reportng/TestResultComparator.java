package org.reportng;

import org.testng.ITestResult;

import java.util.Comparator;

/**
 * @author 李振7
 * Created Time: 2017/11/24 下午4:51
 */
class TestResultComparator implements Comparator<ITestResult> {
    TestResultComparator() {
    }

    @Override
    public int compare(ITestResult result1, ITestResult result2) {
        return result1.getName().compareTo(result2.getName());
    }
}
