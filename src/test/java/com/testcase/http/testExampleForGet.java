package com.testcase.http;

import org.testng.annotations.Test;
import tools.excelread.GetHostIp;
import tools.httprequest.HttpGetTest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 李振7
 * Created Time: 2017/12/18 下午5:15
 *
 */
public class testExampleForGet {
    private Map<String,String> headerMap = new HashMap<String,String>(); //Map<String,String> headerMap,为header列表
    private String url = ""; //String url为post请求的url
    private List<String> responseDataList = new LinkedList<>(); //断言list，元素为String类型
    private String httpStatus = "";
    private String hostIP = "";
    private String hostName = "mcart-go.lemall.com";

    @Test
    public void DoGetTest() throws Exception {
        headerMap.put("User-Agent", "smatisance");
        headerMap.put("Content-type", "application/x-www-form-urlencoded;charset:UTF-8");
        headerMap.put("cookie", "ssouid=2126494707; sso_tk=415dfhjkb5678vhj5jhgasyucibniagcnaohbahdhskhasc");

        hostIP =  new GetHostIp(hostName).getHostIP(); //从host参数化文件中读取HOST IP
//        System.out.println("===================" + hostIP);

        url = "https://ascdsca-go.asd.com/api/query/viewCart.jsonp?asdasd=10002&cityId=10048&arrivalId=10501&toPay=0&deviceid=&version=4.0&rs=1100&_=1503543771058&callback=Zepto1503543771015";

        responseDataList.add("Hellosabiasnas");
        responseDataList.add("lizhen");
        responseDataList.add("bvghkl");
        httpStatus = "200";

        HttpGetTest httpGetTest = new HttpGetTest(headerMap, url, responseDataList, httpStatus, hostIP);

    }
}
