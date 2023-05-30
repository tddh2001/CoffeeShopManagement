package com.cfm.coffeemanagement;

import com.cfm.coffeemanagement.model.response.MailResponse;
import com.cfm.coffeemanagement.service.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.cfm.coffeemanagement.constants.Constants.MAIL_SENDED;

@RunWith(SpringRunner.class)
@SpringBootTest
class MailTest {

    @Autowired
    EmailService emailService;

    @Test
    void testMailSender() {
        String to = "dsktt123@gmail.com";
        String subject = "ACTIVE USER";
        String body = "Hello ";
        MailResponse response = emailService.sendMail(to, subject, body);
        Assertions.assertEquals(MAIL_SENDED, response.getMessage());
    }

}
