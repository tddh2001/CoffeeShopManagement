package com.cfm.coffeemanagement;

import com.cfm.coffeemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserTest {

    @Autowired
    UserService userService;
}
