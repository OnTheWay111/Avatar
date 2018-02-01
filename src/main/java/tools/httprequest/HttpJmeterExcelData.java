package tools.httprequest;

import jxl.read.biff.BiffException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.excelread.GetHostIp;
import tools.excelread.GetTestCaseExcel;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 李振7
 * Created Time: 2018/1/22 上午10:27
 */

public class HttpJmeterExcelData {
    //文件路径src/testExampleForJmeterData/GetTestCaseExcel/dubbo
    public String filePath = "";
    //文件名，不包含文件后缀.xls
    public String fileName = "";
    //sheet名
    public String caseName = "";


    public HttpJmeterExcelData(String filePath, String fileName, String caseName) throws IOException, BiffException {
        this.filePath = filePath;
        this.fileName = fileName;
        this.caseName = caseName;
    }


    @DataProvider
    public Object[][] Numbers() throws BiffException, IOException {
        GetTestCaseExcel e=new GetTestCaseExcel(filePath, fileName, caseName);
        return e.getExcelData();
    }

    @Test(dataProvider = "Numbers")
    public void test(HashMap<String, String> data) throws IOException, BiffException {
        //Map<String,String> paramMap,为post请求的参数列表
        Map<String,String> paramMap = new HashMap<String,String>(100);
        //Map<String,String> headerMap,为header列表
        Map<String,String> headerMap = new HashMap<String,String>(100);
        //String url为post请求的url
        String url = "";
        //断言list，元素为String类型
        List<String> responseDataList = new LinkedList<>();

        String caseTestOrNot = data.get("CaseTestOrNot");
        String lastOrNot = data.get("LastOrNot");
        String caseID = data.get("CaseID");
        String testCaseName = data.get("TestCaseName");
        String protocol = data.get("Protocol");
        String requstMethod = data.get("RequstMethod");
        String domain = data.get("Domain");
        String path = data.get("Path");
        String port = data.get("Port");
        String requstData = data.get("RequstData");
        String httpStatus = data.get("HttpStatus");
        String responseMessage = data.get("ResponseMessage");
        String responseData1 = data.get("ResponseData1").replaceAll("\\\\","");
        String responseData2 = data.get("ResponseData2").replaceAll("\\\\","");
        String responseData3 = data.get("ResponseData3").replaceAll("\\\\","");
        //获取要绑定的hostIP
        String hostIP = "";

        String hostdemo = new GetHostIp(domain).getHostIP();
        //做非空判断，避免空指针
        if (hostdemo!= null && hostdemo.length()>0) {
            hostIP = hostdemo;
        } else {
            hostIP = "";
        }


        //配置Header
        headerMap.put("mEncodeMethod", "none");
        headerMap.put("User-Agent", "LetvShop;1.6.7.0;Letv+X500;android-phone;21;zh_CN");
        headerMap.put("Content-Type", "application/json");
        headerMap.put("cookie", "ssouid=2126494707; sso_tk=102XXXO0EZzx5TDSj2oFRbam2avyAm3El7RLin1zm2tvubfEFUy1cJfHZElJVW3Um30ffBYl7GvOkm2Mfdim38Aaipj2SlKPxORcufgm1O5EyBfhFbP2PDbcm4");

        //配置ResponseData
        if (responseData1.length()>0){
            responseDataList.add(responseData1);
        }
        if (responseData1.length()>0) {
            responseDataList.add(responseData2);
        }
        if (responseData1.length()>0) {
            responseDataList.add(responseData3);
        }
        System.out.println(responseData1 + "\n" + responseData2 + "\n" + responseData3);

        //配置http status
        httpStatus = httpStatus;
        String get = "get";
        String post = "post";
        if (get.equalsIgnoreCase(requstMethod)) {
            // url生成
            if (port == null || port.length() == 0) {
                url = protocol + "://" + domain + path + "?" + requstData;
            } else {
                url = protocol + "://" + domain + ":" + port + path + "?" + requstData;
            }

            try {
                System.out.println(url);
                HttpGetTest httpGetTest = new HttpGetTest(headerMap, url, responseDataList, httpStatus, hostIP);
                if(httpGetTest.isRequestSuccessful()) {
                    Assert.assertTrue(true);
                } else {
                    Assert.assertTrue(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (post.equalsIgnoreCase(requstMethod)) {
            // url生成
            if (port == null || port.length() == 0) {
                url = protocol + "://" + domain + path;
            } else {
                url = protocol + "://" + domain + ":" + port + path;
            }

            //将RequstData字符串分解保存到paramMap中
            String[] requestParam = requstData.split("&", 0);
            for (int i=0; i<requestParam.length; i++) {
                String[] keyValue = requestParam[i].split("=",0);
                paramMap.put(keyValue[0],keyValue[1]);
            }

            try {
                System.out.println(url);
                HttpPostTest httpPostTest = new HttpPostTest(paramMap, headerMap, url, responseDataList, httpStatus, hostIP);
                if(httpPostTest.isRequestSuccessful()) {
                    Assert.assertTrue(true);
                } else {
                    Assert.assertTrue(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    //测试验证
    public static void main (String[] args) {

        //测试验证将RequstData字符串分解保存到paramMap中的正确性
        Map<String,String> paramMap = new HashMap<String,String>();

        String RequstData = "name=lizhen&age=27&city=beijing";
        String[] requestParam = RequstData.split("&", 0);
        System.out.println(requestParam.length);
        System.err.println(requestParam[0] + ", " + requestParam[1]);

        for (int i=0; i<requestParam.length; i++) {
            String[] keyValue = requestParam[i].split("=",0);
            paramMap.put(keyValue[0],keyValue[1]);
       //     System.out.println(keyValue[0] + "," + keyValue[1]);
        }

        Iterator iterator = paramMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("-------------" + key + " = " + value);
        }

    }
    */

}
