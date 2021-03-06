package me.superkoh.kframework.lib.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * Created by KOH on 2017/6/2.
 * <p>
 * k-framework
 */
public class JavaMailMessage {

    private String smtpHost;
    private String fromAddress;
    private String username;
    private String password;

    private Properties properties;

    public JavaMailMessage(String smtpHost, String fromAddress, String username, String password) {
        this.smtpHost = smtpHost;
        this.fromAddress = fromAddress;
        this.username = username;
        this.password = password;
        properties = System.getProperties();
        properties.setProperty("mail.smtp.host", smtpHost);
        properties.setProperty("mail.smtp.auth", "true");
    }

    public void sendMail(String to, String title, String content) throws MessagingException {
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(title);
        message.setContent(content, "text/html;charset=UTF-8");
        Transport.send(message);
    }

    public void sendMailBatch(List<String> to, String title, String content) throws MessagingException {
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress));
        to.forEach(address -> {
            try {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            } catch (MessagingException ignored) {
            }
        });
        message.setSubject(title);
        message.setContent(content, "text/html;charset=UTF-8");
        Transport.send(message);
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
