package com.hawaii.epc.email.entity;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.SessionScoped;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Properties;

@Slf4j
@SessionScoped
public class EmailSender implements Serializable {

    @Resource(name = "mail/HawaiiGmailSession")
    private Session mailSession;

    Properties emailProperties = new Properties();

    public void sendEmail(String toEmail, String subject, String messageBody) {
            try {
                emailProperties.put("mail.smtp.auth", "true");
                emailProperties.put("mail.smtp.starttls.enable", "true");
                emailProperties.put("mail.smtp.host", "smtp.gmail.com");
                emailProperties.put("mail.smtp.port", "587");
                // Create a session with Gmail's SMTP server
                Session session = Session.getInstance(emailProperties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        // Replace with your Gmail credentials
                        return new PasswordAuthentication("mmatellan@epc-instore.com", "myfg fake keyh ojlf");
                    }
                });

                // Create email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("mmatellan@epc-instore.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(subject);
                message.setText(messageBody);

                // Send the message
                Transport.send(message);

                System.out.println("Email sent successfully!");

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
