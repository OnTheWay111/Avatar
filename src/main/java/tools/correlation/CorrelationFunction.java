package tools.correlation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 李振7
 * Created Time: 2017/12/22 下午3:43
 * 关联函数
 */
public class CorrelationFunction {
    String leftBoundary = "";
    String rightBoundary = "";

    public String getCorrelationStr(String s, String leftBoundary, String rightBoundary) {
        // 按指定模式在字符串查找
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        String pattern = ".*" + leftBoundary + "(.*)" + rightBoundary +".*";
        String correlationStr = "";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(s);


        if (m.find()) {
            correlationStr = m.group(1);
            return correlationStr;
        } else {
            System.err.println("--------------------Warning: 没有在指定字符串中找到可关联的字符串---------------------");
            return correlationStr;
        }

    }

//    public static void main(String[] args) {
//        CorrelationFunction correlationFunction = new CorrelationFunction();
//        System.out.println(correlationFunction.GetCorrelationStr("#111%#2222%", "#", "%"));
//
//    }

}
