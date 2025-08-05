package com.example.Test.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {
 private final JavaMailSender mailSender;

 public EmailService (JavaMailSender mailSender){
     this.mailSender=mailSender;
 }

 public void sendMail(String to,String subject,String body){
     SimpleMailMessage message = new SimpleMailMessage();
     message.setTo(to);
     message.setFrom("store@test.link");
     message.setSubject(subject);
     message.setText(body);
     mailSender.send(message);
 }

}
