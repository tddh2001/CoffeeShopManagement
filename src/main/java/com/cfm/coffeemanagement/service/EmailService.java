package com.cfm.coffeemanagement.service;

import com.cfm.coffeemanagement.model.response.MailResponse;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.cfm.coffeemanagement.constants.Constants.MAIL_SENDED;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public MailResponse sendMail(String to, String subject, String body) {
        try {
            MailResponse response = new MailResponse();
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessage.setContent(body, "text/html");
            javaMailSender.send(mimeMessage);

            response.setMessage(MAIL_SENDED);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
