package com.example.bootstoreproject.config;

import com.example.bootstoreproject.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService service;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(service);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests()
                .requestMatchers("/","/home","/sign-up","/register",
                        "/check-out-v1","/cart/add-cart","/view-cart",
                        "/view-cart/remove","/view-cart/clear","/book/details","/check-out-v2").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .failureUrl("/login-error")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout")
                .permitAll();



        http.csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

//        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
