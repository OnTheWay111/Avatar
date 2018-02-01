package tools.httprequest;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 测试HttpClient发送各种请求的方法
 * 支持HTTP/HTTPS GET请求
 *
 * @author 李振7
 * Created Time: 2017/12/18 上午10:27
 */
public class HttpGetTest {
    //Map<String,String> headerMap,为header列表
    private Map<String,String> headerMap = new HashMap<String,String>();
    //String url为post请求的url
    private String url = "";
    //断言list，元素为String类型
    private List<String> responseDataList = new LinkedList<>();
    private String httpStatus = "";
    private HttpResponse httpresponse = null;
    private String responseBody = "";
    private String hostIP = "";

    /**
     * 构造函数
     */
    public HttpGetTest(Map<String,String> headerMap, String url, List<String> responseDataList, String httpStatus, String hostIP) throws Exception {
        this.headerMap = headerMap;
        this.url = url;
        //做非空判断，避免空指针
        if (hostIP!= null && hostIP.length()>0) {
            this.hostIP = hostIP;
        }
        this.responseDataList = responseDataList;
        this.httpStatus = httpStatus;
        httpresponse = httpGetRequest();
        responseBody = EntityUtils.toString(httpresponse.getEntity(), "utf-8");
        isRequestSuccessful();
    }

    public Boolean isRequestSuccessful() throws Exception {
        //以UTF-8格式，返回包含内容的字符串
        //HttpResponse.getEntity()，返回响应实体，如果没有，则为null
        int i = responseDataList.size();
        int j = 0;
        boolean isRequestSuccessful = false;
        if(responseDataList.size()>0) {
            for (String responseData : responseDataList) {
                CharSequence s1 = responseData.subSequence(0, responseData.length());
                if (responseBody.contains(s1)) {
                    j++;  //遍历断言，每次断言成功，j+1
                } else {
                    Reporter.log(">>>>>>>>>>>>>>>>>>>>断言失败的ResponseData是：" + responseData + "<<<<<<<<<<<<<<<<<<<<");
                    System.out.println(">>>>>>>>>>>>>>>>>>>>断言失败的ResponseData是：" + responseData + "<<<<<<<<<<<<<<<<<<<<");
                }
            }
        }
        Integer realHttpStatus = httpresponse.getStatusLine().getStatusCode();
        String s = realHttpStatus.toString();
        boolean statusAssertion = httpStatus.equals(s);
        if (statusAssertion) {
            Reporter.log("HttpStatus断言成功");
            //断言
            if (i == j) {
                isRequestSuccessful = true;
                Reporter.log("所有ResponseData均断言成功");
            } else {
                Reporter.log(">>>>>>>>>>>>>>>>>>>>ResponseData断言失败<<<<<<<<<<<<<<<<<<<<");
                System.out.println(">>>>>>>>>>>>>>>>>>>>ResponseData断言失败<<<<<<<<<<<<<<<<<<<<");
            }
        } else {
            isRequestSuccessful = false;
            Reporter.log(">>>>>>>>>>>>>>>>>>>>" + "状态码校验失败，实际返回状态码为：" + httpresponse.getStatusLine().getStatusCode()
                    + "\n断言的状态码httpStatus为：" + httpStatus + "<<<<<<<<<<<<<<<<<<<<");
            System.out.println(">>>>>>>>>>>>>>>>>>>>" + "状态码校验失败，实际返回状态码为：" + httpresponse.getStatusLine().getStatusCode()
                    + "\n断言的状态码httpStatus为：" + httpStatus + "<<<<<<<<<<<<<<<<<<<<");
        }



        try {
            if(isRequestSuccessful) {
                System.out.println("请求成功的接口URL为：\n" + url);
                Reporter.log("请求成功的接口URL为：\n" + url);
                System.out.println("responseBody: \n" + responseBody);
                Reporter.log("responseBody: \n" + responseBody);
                Assert.assertTrue(true);
            } else {
                System.err.println("请求失败的接口URL为：\n" + url);
                Reporter.log("请求失败的接口URL为：\n" + url);
                System.err.println("responseBody: \n" + responseBody);
                Reporter.log("responseBody: \n" + responseBody);
                Assert.assertTrue(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isRequestSuccessful;
    }


    public HttpResponse httpGetRequest() throws Exception{
        //基于要发送的HTTP请求类型创建HttpGet或者HttpPost实例
        HttpGet httpGet = new HttpGet(url);

        //get请求，添加header
        for(Map.Entry<String,String> entry:headerMap.entrySet()){
            httpGet.addHeader(entry.getKey(),entry.getValue());
        }

        //创建HttpClient对象，HttpClients.createDefault()。
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse httpresponse = null;


        //做非空判断，避免空指针
        if (hostIP!= null && hostIP.length()>0) {
            //绑定host
            HttpHost httpHost = new HttpHost(hostIP);

            // 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse
            httpresponse = httpClient.execute(httpHost, httpGet);
        } else {
            // 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse
            httpresponse = httpClient.execute(httpGet);
        }
        return httpresponse;
    }

    public String getResponseHeader() throws Exception{
        return httpresponse.toString();
    }

    public String getResponseBody() throws Exception{
        return responseBody;
    }

    public String getHttpStatus() throws Exception{
        Integer realHttpStatus = httpresponse.getStatusLine().getStatusCode();
        String s = realHttpStatus.toString();
        return s;
    }

}
