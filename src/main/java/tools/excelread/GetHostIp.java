package tools.excelread;

import jxl.read.biff.BiffException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author 李振7
 * Created Time: 2018/1/23 下午6:54
 */
public class GetHostIp {
    private static final String PROPERTIES_DEFAULT = "environmentConfig.properties";
    //sheet名
    private static String caseName = "";
    private String hostName = "";
    private GetHostIpExcel getHostIpExcel;
    private HashMap<String, String> hostNameIpMap = new HashMap<String,String>();
    private static Properties properties;




    public GetHostIp(String hostName) throws IOException, BiffException {
        properties = new Properties();
        InputStream inputStream = null;
        inputStream = GetHostIp.class.getClassLoader().getResourceAsStream(PROPERTIES_DEFAULT);
        properties.load(inputStream);
        inputStream.close();
        //确定要测试环境，online,test,yulan
        caseName = properties.getProperty("testEnvironment");

        this.hostName = hostName;
        getHostIpExcel = new GetHostIpExcel(caseName);
        hostNameIpMap = getHostIpExcel.getHostNameIpMap();
    }

    public String getHostIP() {
        String hostIp = "";
        String hostdemo = hostNameIpMap.get(hostName);
        if (hostdemo!= null && hostdemo.length()>0 ) {
            hostIp = hostdemo;
        } else {
            hostIp = "";
        }
        return hostIp;
    }

/*  测试代码
    public static void main (String[] args) throws IOException, BiffException {
        String HostName = "m.go.le.com";
        String hostIP = new GetHostIp(HostName).getHostIP();
        System.out.println(hostIP);
    }
*/

}
