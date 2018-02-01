package tools.correlation;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 李振7
 * Created Time: 2018/1/31 下午4:15
 * 功能描述：获取所有正则匹配内容,并保存在List中
 */
public class getMatchers {
    //regex是正则表达式，source为资源字符串
    public List<String> getMatchers(String regex, String source){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            for(int i=1; i<=matcher.groupCount(); i++){
             //   System.out.println(i+":-------------"+matcher.group(i));
                list.add(matcher.group(i));
            }

        }
        return list;
    }

    @Test
    public void test() {
        String regex = "[\\s\\S]?method=([\\s\\S]+?)\\[pri[\\s\\S]+?[,\\s]+";
        getMatchers getMatchers = new getMatchers();
        List<String> s = getMatchers.getMatchers(regex, "[[TestResult name=cancelOrder status=FAILURE method=pcOrderAllProcess.cancelOrder()[pri:24, instance:com.testcase.http.online.pc.pcOrderAllProcess@38bc8ab5] output=>>>>>>>>>>>>>>>>>>>>断言失败的ResponseData是：status\":\"1<<<<<<<<<<<<<<<<<<<<]][[TestResult name=cancelOrder status=FAILURE method=pcOrderAllProcess.cancelOrder1111()[pri:24, instance:com.testcase.http.online.pc.pcOrderAllProcess@38bc8ab5] output=>>>>>>>>>>>>>>>>>>>>断言失败的ResponseData是：status\":\"1<<<<<<<<<<<<<<<<<<<<]]\n");
        for (int i=0; i<s.size(); i++) {
            System.out.println(s.get(i));
        }
    }
}
