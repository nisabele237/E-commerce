package com.example.Test.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.reactive.GenericReactiveTransaction;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Login {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 Long id;
 String email;
 String password;

}
