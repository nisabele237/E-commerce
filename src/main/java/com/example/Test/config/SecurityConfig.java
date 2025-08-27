package com.example.Test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         http
                 .csrf(csrf -> csrf.disable())
                 .authorizeHttpRequests(requests->requests
                         .requestMatchers("/store/compte/user").permitAll()
                         .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/images/**").permitAll()
                         .requestMatchers("/store/validation/user/{email}/{code}").permitAll()
                         .requestMatchers("/store/login/user").permitAll()
                         .requestMatchers(
                                 "/v3/api-docs/**",
                                 "/swagger-ui/**",
                                 "/swagger-ui.html"
                         ).permitAll()
                         .requestMatchers("/store/topic/**",
                                 "/topic/**",
                                 "/store/app/**",
                                 "/app/**",
                                         "/chat/**",
                                 "/store/sendMessage",
                                 "/sendMessage",
                                 "/appchat",
                                 "/historique",
                                 "/chat/**"
                         )
                         .permitAll()
                         .anyRequest().authenticated()
                 )
                 .oauth2Login(Customizer.withDefaults());
         return http.build();

 }
 }







