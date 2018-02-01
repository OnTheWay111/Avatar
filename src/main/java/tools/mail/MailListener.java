package tools.mail;

import jxl.read.biff.BiffException;
import tools.filetozip.FileToZip;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 李振7
 * Created Time: 2017/11/24 下午3:07
 */
public class MailListener {
    /**
     * 邮件主题
     */
    public String subject;
    /**邮件组*/
    public InternetAddress[] testAddressList;
    /**邮件内容*/
    public String mailHtml;

    public MailListener() throws AddressException, IOException, BiffException {
        testAddressList = new TestAddressList().getAddressList();
    }

    public void failMailTest() {
        /*
            压缩报告文件
         */
            Date now = new Date();

        //可以方便地修改日期格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String datatime = dateFormat.format(now);
            String sourceFilePath = "target/surefire-reports/html";
            String zipFilePath = "target/surefire-reports/";
            String fileName = "ReportHTMLFile" + datatime;
            boolean flag = FileToZip.fileToZip(sourceFilePath, zipFilePath, fileName);
            if (flag) {
                System.out.println("文件打包成功!");
            } else {
                System.out.println("文件打包失败!");
            }

        /*
        发送邮件
         */
            String filePath = "target/surefire-reports/" + fileName + ".zip";
            MailUtil mailUtil = new MailUtil();

            try {
                mailUtil.sendMail(testAddressList, subject, mailHtml, fileName+ ".zip", filePath);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
    }

}

