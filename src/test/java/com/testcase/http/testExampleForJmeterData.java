package com.testcase.http;

import jxl.read.biff.BiffException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.httprequest.HttpJmeterExcelData;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 李振7
 * Created Time: 2018/1/22 下午3:58
 */

public class testExampleForJmeterData {
    String filePath = "/src/test/TestCaseExcelData/http/";  //文件路径src/testExampleForJmeterData/GetTestCaseExcel/dubbo
    String fileName = "testcase"; //文件名，不包含文件后缀.xls
    String caseName = "testcase"; //sheet名
    public HttpJmeterExcelData httpJmeterExcelDatademo;
    public testExampleForJmeterData() throws IOException, BiffException {
        httpJmeterExcelDatademo = new HttpJmeterExcelData(filePath,fileName,caseName);
    }

    @DataProvider
    public Object[][] Numbers() throws BiffException, IOException {
        return httpJmeterExcelDatademo.Numbers();
    }

    @Test(dataProvider = "Numbers")
    public void test(HashMap<String, String> data) throws IOException, BiffException {
        httpJmeterExcelDatademo.test(data);
    }
}
