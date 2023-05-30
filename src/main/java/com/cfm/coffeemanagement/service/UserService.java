package com.cfm.coffeemanagement.service;

import com.cfm.coffeemanagement.constants.Constants;
import com.cfm.coffeemanagement.filter.JWTAuthFilter;
import com.cfm.coffeemanagement.model.db.User;
import com.cfm.coffeemanagement.model.request.*;
import com.cfm.coffeemanagement.model.response.*;
import com.cfm.coffeemanagement.repository.UserRepository;
import com.cfm.coffeemanagement.utils.EmailValidation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.cfm.coffeemanagement.constants.Constants.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final JWTAuthFilter jwtAuthFilter;


    public UserService(UserRepository userRepository, JWTService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, EmailService emailService, JWTAuthFilter jwtAuthFilter) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    public SignUpResponse signUp(SignUpRequest request) {
        SignUpResponse response = new SignUpResponse();
        try {
            request.validate();
            EmailValidation.validate(request.getEmail());
        } catch (IllegalArgumentException e) {
            response.setMessage(e.getMessage());
            return response;
        }
        Optional<User> userOpt = userRepository.findByName(request.getUsername());
        Optional<User> emailOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            try {
                throw new IllegalAccessException();
            } catch (IllegalAccessException e) {
                response.setMessage(USER_EXISTED);
                return response;
            }
        }
        if (emailOpt.isPresent()) {
            try {
                throw new IllegalAccessException();
            } catch (IllegalAccessException e) {
                response.setMessage(EMAIL_EXISTED);
                return response;
            }
        }
        User user = new User();
        user.setName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setStatus(INACTIVE);
        user.setRole(request.getRoles());
        User newUser = userRepository.save(user);

        emailService.sendMail(request.getEmail(), SUBJECT_USER_ACTIVE, TEXT_BODY_USER_ACTIVE + "<a href=\"" +API_ACTIVE+ newUser.getId()+"\">Click here</a>");
        response.setMessage(SIGNUP_SUCCESSFULLY);
        return response;
    }

    public SignInResponse signIn(SignInRequest request) {
        SignInResponse response = new SignInResponse();
        try {
            request.validate();
        } catch (IllegalArgumentException e) {
            response.setMessage(e.getMessage());
            return response;
        }
        Optional<User> userOpt = userRepository.findByName(request.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean checkPassword = encoder.matches(request.getPassword(), user.getPassword());
            if (checkPassword) {
                if (user.getStatus().equalsIgnoreCase(ACTIVE)) {
                    String token = auth(request);
                    response.setToken(token);
                    response.setMessage(LOGIN_SUCCESS);
                } else {
                    response.setMessage(USER_IS_NOT_ACTIVATED);
                }
            } else {
                response.setMessage(INCORRECT_PASSWORD);
            }
        } else {
            response.setMessage(USER_NOT_FOUND);
        }
        return response;
    }

    public String auth(SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(request.getUsername());
        } else {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }
    }

    public ActiveResponse activeUser(Integer id) {
        ActiveResponse response = new ActiveResponse();
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(ACTIVE);
            userRepository.save(user);
            response.setMessage(ACTIVE_USER_SUCCESS);
        }
        else {
            response.setMessage(USER_NOT_FOUND);
        }
        return response;
    }

    public UserResponse changePassword(ChangePasswordRequest request) {
        UserResponse response = new UserResponse();

        Optional<User> userOpt = userRepository.findByName(jwtAuthFilter.getCurrentUser());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean checkPassword = encoder.matches(request.getOldPassword(), user.getPassword());
            if (!checkPassword) {
                response.setMessage(INCORRECT_PASSWORD);
            } else {
                checkPassword = encoder.matches(request.getNewPassword(), user.getPassword());
                if (checkPassword) {
                    response.setMessage(Constants.NEW_PASSWORD_MUST_BE_DIFFERENT_OLD_PASSWORD);
                } else {
                    user.setPassword(encoder.encode(request.getNewPassword()));
                    userRepository.save(user);
                    response.setMessage(CHANGE_PASSWORD_SUCCESS);
                }
            }
        } else {
            response.setMessage(USER_NOT_FOUND);
        }
        return response;
    }

    public UserResponse forgotPassword(ForgotPasswordRequest request) {
        UserResponse response = new UserResponse();
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String newPassword = generateRandomString(9);
            emailService.sendMail(request.getEmail(), SUBJECT_FORGOT_PASSWORD, TEXT_BODY_FORGOT_PASSWORD + newPassword);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            response.setMessage(PLEASE_CHECK_YOUR_MAIL);
        } else {
            response.setMessage(EMAIL_NOT_FOUND);
        }
        return response;
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        char[] randomChars = new char[length];

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomChars[i] = characters.charAt(index);
        }
        return new String(randomChars);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public UpdateEmailResponse updateEmail(UpdateEmailRequest request) {
        UpdateEmailResponse response = new UpdateEmailResponse();
        EmailValidation.validate(request.getEmail());

        Optional<User> userOpt = userRepository.findByName(jwtAuthFilter.getCurrentUser());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEmail(request.getEmail());
            userRepository.save(user);
            response.setMessage(EMAIL_UPDATED_SUCCESSFULLY);
        }
        return response;
    }
}
