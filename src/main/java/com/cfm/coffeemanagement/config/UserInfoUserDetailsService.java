package com.cfm.coffeemanagement.config;

import com.cfm.coffeemanagement.model.db.User;
import com.cfm.coffeemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.cfm.coffeemanagement.constants.Constants.USER_NOT_FOUND;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByName(username);
        return userOpt.map(UserInfoUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException(USER_NOT_FOUND +" "+ username));
    }
}
