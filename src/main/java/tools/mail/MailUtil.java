package tools.mail;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author 李振7
 * Created Time: 2017/10/23 上午10:25
 */
public class MailUtil {
    private static final String HOST = MailConfig.host;
    private static final Integer PORT = MailConfig.port;
    private static final String USERNAME = MailConfig.userName;
    private static final String PASSWORD = MailConfig.passWord;
    private static final String EMAIL_FORM = MailConfig.emailForm;
    private static final String TIMEOUT = MailConfig.timeout;
    private static final String PERSONAL = MailConfig.personal;

    /**
     * 邮件发送器
     *
     * @return 配置好的工具
     */
    private static JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", TIMEOUT);
        p.setProperty("mail.smtp.auth", "false");
        sender.setJavaMailProperties(p);
        return sender;
    }

    private static JavaMailSenderImpl mailSender = createMailSender();

    /**
     * 发送邮件
     *
     * @param to 接受人
     * @param subject 主题
     * @param mailHtml 发送html内容
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public void sendMail(InternetAddress[] to, String subject, String mailHtml, String fileName, String filePath) throws MessagingException,UnsupportedEncodingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        messageHelper.setFrom(EMAIL_FORM, PERSONAL);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(mailHtml, true);
        messageHelper.addAttachment(fileName,new File(filePath));

        mailSender.send(mimeMessage);
    }
}
