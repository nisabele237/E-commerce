package com.example.Test.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String designation;
    private int quantite;
    private int prixunitaire;
    private String categorie;
    private int nombrecommande;
    private boolean status;
    @ManyToMany
    @JsonIgnore
    private List<Utilisateur> utilisateurList;


    public Produit(String designation,int quantite,int prixunitaire, String categorie) {
        this.quantite = quantite;
        this.designation = designation;
        this.prixunitaire=prixunitaire;
        this.categorie = categorie;
        this.nombrecommande = 0;
        this.status = true;
    }
    //Modification du statut d'un produit
    public boolean statusverse(){
        return  !this.status;
    }
}
