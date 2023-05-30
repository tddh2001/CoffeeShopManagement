package com.cfm.coffeemanagement.config;

import com.cfm.coffeemanagement.filter.JWTAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.cfm.coffeemanagement.constants.Constants.ADMIN;
import static com.cfm.coffeemanagement.constants.Constants.USER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JWTAuthFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(@Lazy JWTAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests().requestMatchers("/api/authentication/**").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/welcome/").hasAnyRole(USER,ADMIN)
                .and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/api/user/").hasAnyRole(USER,ADMIN)
                .and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.PUT, "/api/user/").hasAnyRole(USER,ADMIN)
                .and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/api/category/").hasAnyRole(USER,ADMIN)
                .and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/api/category/").hasRole(ADMIN)
                .and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.PUT,"/api/category/").hasRole(ADMIN)
                .and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.DELETE,"/api/category/").hasRole(ADMIN)
                .anyRequest()
                .authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
