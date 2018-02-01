package com.testcase.http;

import org.testng.annotations.Test;
import tools.excelread.GetHostIp;
import tools.httprequest.HttpPostTest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 李振7
 * Created Time: 2017/12/18 下午5:15
 */

public class testExampleForPost {
    private Map<String,String> paramMap = new HashMap<String,String>();  //Map<String,String> paramMap,为post请求的参数列表
    private Map<String,String> headerMap = new HashMap<String,String>(); //Map<String,String> headerMap,为header列表
    private String url = ""; //String url为post请求的url
    private List<String> responseDataList = new LinkedList<>(); //断言list，元素为String类型
    private String httpStatus = "";
    private String hostIP = "";
    private String hostName = "mproduct-go.lemall.com";

    @Test
    public void DoPostTest() throws Exception {
        //request Body
        paramMap.put("params","{\"mobileHead\":\"mobileBody\":{\"proNo\":\"GWGT550623\",\"skuNo\":\"\"}}");

        //request header
        headerMap.put("cookie", "ssouid=1234567892345678; sso_tk=415dfhjkb5678vhj5jhgasyucibniagcnaohbahdhskhasc");
        headerMap.put("mEncodeMethod", "none");
        headerMap.put("User-Agent", "android-phone;21;zh_CN");

        //从host ip参数化文件中获取hostname对应的host ip
        hostIP =  new GetHostIp(hostName).getHostIP();

        //post请求的URL
        url = "https://adsad-go.asdasdas.com:443/api/query/v2/asdasd.json?sso_tk=415dfhjkb5678vhj5jhgasyucibniagcnaohbahdhskhasc";

        //需要校验的response data
        responseDataList.add("message\":\"服务调用成功\"");

        //需要校验的http状态码
        httpStatus = "200";

        //调用http post请求类
        HttpPostTest httpPostTest = new HttpPostTest(paramMap, headerMap, url, responseDataList, httpStatus, hostIP);

    }
}
