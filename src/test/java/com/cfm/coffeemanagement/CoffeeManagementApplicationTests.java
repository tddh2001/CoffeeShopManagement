package com.cfm.coffeemanagement;

import com.cfm.coffeemanagement.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoffeeManagementApplicationTests {

    @Autowired
    EmailService emailService;

    @Test
    void testMailSender() {
        String to = "dsktt123@gmail.com";
        String subject = "ACTIVE USER";
        String body = "Hello ";
        emailService.sendMail(to, subject, body);
    }

}
