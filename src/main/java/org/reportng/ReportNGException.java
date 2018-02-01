package org.reportng;

/**
 * @author 李振7
 * Created Time: 2017/11/24 下午4:49
 */
public class ReportNGException extends RuntimeException {
    public ReportNGException(String string) {
        super(string);
    }

    public ReportNGException(String string, Throwable throwable) {
        super(string, throwable);
    }
}