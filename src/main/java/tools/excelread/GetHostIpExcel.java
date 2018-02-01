package tools.excelread;

import jxl.read.biff.BiffException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 李振7
 * Created Time: 2018/1/23 下午6:01
 */

public class GetHostIpExcel {
    //文件路径
    private String filePath = "/src/test/TestCaseExcelData/HostIP/";
    //文件名，不包含文件后缀.xls
    private String fileName = "HostIP";
    //sheet名
    private String caseName = "online";
    private GetTestCaseExcel e;
    private HashMap<String, String>[][] hostMap;
    private HashMap<String, String> hostNameIpMap = new HashMap<String,String>();

    public GetHostIpExcel(String caseName) throws IOException, BiffException {
        this.caseName = caseName;
        e=new GetTestCaseExcel(filePath, fileName, caseName);
        hostMap = (HashMap<String, String>[][]) e.getExcelData();
    }

    public HashMap<String, String> getHostNameIpMap() throws IOException, BiffException {
        for (HashMap<String, String>[] data : hostMap) {
            for (HashMap<String, String> data1 : data) {
                String hostip = "";
                String hostname = "";
                for(Map.Entry<String, String> entry: data1.entrySet())
                {
                    if("HostIP".equalsIgnoreCase(entry.getKey())) {
                        hostip = entry.getValue();
                    } else if ("HostName".equalsIgnoreCase(entry.getKey())) {
                        hostname = entry.getValue();
                    }
                }
                System.out.println("HOST配置为：" + hostname + " = " + hostip);
                hostNameIpMap.put(hostname, hostip);
            }
        }

        return hostNameIpMap;
    }



//    public HashMap<String, String> GetHostMap() {
//        HashMap<String, String> hostHashMap = (HashMap<String, String>) hostMap;
//        return hostHashMap;
//    }

//    public String GetHostIp(String hostNameKey) {
//        HashMap<String, String> hostHashMap1 = (HashMap<String, String>) hostMap;
//        String HostIPValue = "";
//
//        for(Map.Entry<String, String> entry: hostHashMap1.entrySet())
//        {
//            if (entry.getKey().equalsIgnoreCase(hostNameKey)) {
//                HostIPValue = entry.getValue();
//            } else {
//                HostIPValue = "";
//            }
//        }
//        return HostIPValue;
//    }

}
