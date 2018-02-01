package tools.mail;

import jxl.read.biff.BiffException;
import org.testng.annotations.Test;
import tools.excelread.GetTestCaseExcel;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 李振7
 * Created Time: 2017/12/8 下午3:17
 */
public class TestAddressList {
    //邮件组
    private InternetAddress[] testAddressList;
    private ArrayList<String> emailList = new ArrayList<String>();
    //文件路径src/testExampleForJmeterData/GetTestCaseExcel/dubbo
    private String filePath = "/src/test/TestCaseExcelData/emailList/";
    //文件名，不包含文件后缀.xls
    private String fileName = "emailList";
    //sheet名
    private String caseName = "emailList";
    private GetTestCaseExcel e;
    private HashMap<String, String>[][] hostMap;

    @Test
    public TestAddressList() throws IOException, BiffException, AddressException {
        this.caseName = caseName;
        e=new GetTestCaseExcel(filePath, fileName, caseName);
        hostMap = (HashMap<String, String>[][]) e.getExcelData();
        getAddressList();
    }

    public InternetAddress[] getAddressList() throws AddressException {
        for (HashMap<String, String>[] data : hostMap) {
            for (HashMap<String, String> data1 : data) {
                for(Map.Entry<String, String> entry: data1.entrySet())
                {
                    String emailStr = entry.getValue();
                    if (emailStr!=null && emailStr.length()>0) {
                        emailList.add(emailStr);
                    }
                }
            }
        }
        int emailListSize = emailList.size();
        if (emailListSize>0) {
            testAddressList = new InternetAddress[emailList.size()];
        }
        for(int i=0; i<emailListSize; i++) {
            String email = emailList.get(i);
            if (email!=null && email.length()>0) {
                testAddressList[i] = new InternetAddress(email);
            }
        }
        return testAddressList;
    }


}