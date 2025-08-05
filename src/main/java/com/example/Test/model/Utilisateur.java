package com.example.Test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    private String code;
    private String password;
    private boolean status;
    private boolean log;
    private int montant;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToMany
    private List<Produit> produitList;
    @OneToMany
    private List<Produit> produitFavoris;


    public Utilisateur(String nom, String password, Role role) {
        this.nom = nom;
        this.password = password;
        this.role = role;
    }

    public boolean statusverse(){ return !this.status;}
    public boolean logverse(){ return !this.log;}
}
