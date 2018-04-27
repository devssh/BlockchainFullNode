package app.service;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MailService {

    public static void SendMailWithConfirmationCode(String to, String subject, String text) {

        try {
            JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
            emailSender.setHost("smtp.gmail.com");
            emailSender.setPort(587);

            emailSender.setUsername("deva3sood@gmail.com");
            emailSender.setPassword("nopass");

            Properties props = emailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
