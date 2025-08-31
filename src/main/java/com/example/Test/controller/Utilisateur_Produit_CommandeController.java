package com.example.Test.controller;

import com.example.Test.dto.Login;
import com.example.Test.model.ChatMessage;
import com.example.Test.model.Produit;
import com.example.Test.model.Utilisateur;
import com.example.Test.repository.ChatRepository;
import com.example.Test.service.Utilisateur_Produit_CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/store")
@Tag(name ="Gestion des utilisateurs",description="Endpoints pour la gestion des utilisateurs")
@Tag(name="Gestion des produits", description="Endpoints pour la gestion des produits")
@Tag(name="Gestion des Commandes et Précommande", description="Endpoints pour la gestion des produits commandés par les clients et leurs paniers")

public class Utilisateur_Produit_CommandeController
{
    @Autowired
    private  Utilisateur_Produit_CommandeService utilisateurProduitCommandeService;


    @GetMapping("/public")
    public String Public (){
        return "Accès public";
    }

    @PostMapping("/compte/user")
    @Operation(
            summary = "Demande de Création de Compte",
            description = "Envoie d'un code à l'adresse de l'utilisateur qui lui permettra de valider la creation du compte",
            tags = {"Gestion des utilisateurs"}
    )
    public ResponseEntity<?> createutilisateur(@RequestBody Utilisateur user){
        System.out.println("hi");
        return utilisateurProduitCommandeService.createutilisateur(user);
    }
    @PostMapping("/validation/user/{email}/{code}")
    @Operation(
            summary = "Validation de la creation de Compte",
            description = "L'utilisateur doit valider le code qui lui a été envoyé par mail ce qui confirme le creation de son compte"
            ,tags = {"Gestion des utilisateurs"}
    )
    public ResponseEntity<?> validutilisateur(@PathVariable String email,@PathVariable String code){
        return utilisateurProduitCommandeService.validUtilisateur(email,code);
    }
    @PostMapping("/login/user")
    @Operation(
            summary = "Enpoint de connexion à un compte existant",
            description = "Permet de ce connecter via son email et son mot de passe",
            tags = {"Gestion des utilisateurs"}
    )

    public ResponseEntity<?> loginutilisateur(@RequestBody Login login){
        String email = login.getEmail();
        String password = login.getPassword();
        return utilisateurProduitCommandeService.logInUser(email,password);
    }
    @PostMapping("/logout/user/{email}")
    @Operation(
            summary = "Enpoint de déconnexion à un compte existant",
            description = "Permet de ce connecter via son email",
            tags = {"Gestion des utilisateurs"}
    )
    public ResponseEntity<?> logoututilisateur(@PathVariable String email){
        return utilisateurProduitCommandeService.logOutUser(email);
    }

    @GetMapping("/user")
    @Operation(
            summary = "Liste de utilisateurs ",
            tags = {"Gestion des utilisateurs"}
    )

    public ResponseEntity<?> getallutilisateur(){
        return utilisateurProduitCommandeService.findAllUser();
    }




    @GetMapping("/produit")
    @Operation(
            summary = "Lister tous les produits disponibles",
            tags = {"Gestion des produits"}

    )
    public List<Produit>getallproduit(){
        return utilisateurProduitCommandeService.findAllProduit();
    }

    @PostMapping("/produit")
    @Operation(
            summary = "Création d'un produit",
            tags = {"Gestion des produits"}
    )
    public Produit createproduit( @RequestBody Produit produit){
        return utilisateurProduitCommandeService.createproduit(produit);
    }
    @PutMapping("update/{idproduit}")
    @Operation(
            summary = "Mis à jour des informations d'un produit",
            tags = {"Gestion des produits"}

    )
    public ResponseEntity<?> updateProduct(@RequestBody Produit produit,@PathVariable Long idproduit){
        return utilisateurProduitCommandeService.updateProduct(produit,idproduit);
    }
    @PostMapping ("/restock/{iduser}/{idproduit}/{quantite}")
    @Operation(
            summary = "Réapprovisonner les stocks",
            description = "*** Accès reservé aux ADMIN",
            tags = {"Gestion des produits"}

    )
    public ResponseEntity<?> lienAdmin(@PathVariable(name = "iduser") Long idadmin,@PathVariable  Long idproduit,@PathVariable int quantite){
        return utilisateurProduitCommandeService.lienAdmin(idadmin,idproduit,quantite);
    }
    @DeleteMapping ("/destock/{iduser}/{idproduit}")
    @Operation(
            summary = "Retirer un produit du stock de manière définitive",
            description = "*** Accès reservé aux ADMIN",
            tags = {"Gestion des produits"}

    )
    public ResponseEntity<?> lienAdmin(@PathVariable(name = "iduser") Long idadmin, @PathVariable  Long idproduit){
        return utilisateurProduitCommandeService.DeleteProduit(idadmin,idproduit);
    }

    @GetMapping ("/produit/{idproduit}")
    @Operation(
            summary = "Retrouver un produit par son identifiant",
            tags = {"Gestion des produits"}

    )
    public ResponseEntity<?> findProduit(@PathVariable (name = "idproduit") Long id){
        return utilisateurProduitCommandeService.findProductById(id);
    }
    @GetMapping("/categorie/{categorie}")
    @Operation(
            summary = "Retrouver un produit par sa categorie",
            tags = {"Gestion des produits"}

    )
    public ResponseEntity<?> findByCategorie(@PathVariable String categorie){
        return utilisateurProduitCommandeService.findProduitByCategorie(categorie);
    }


    @GetMapping("consult/{iduser}")
    @Operation(
            summary = "Consulter les produits populaires(déja été achetés au moins une fois)",
            description = "*** Accès reservé aux ADMIN",
            tags = {"Gestion des produits"}

    )
    public ResponseEntity<?> consultProduct (@PathVariable Long iduser){
        return utilisateurProduitCommandeService.consultProduit(iduser);
    }


    @PostMapping("/user/{iduser}/{idproduit}/{quantite}")
    @Operation(
            summary = "Commander un produit",
            description = "*** Accès reservé aux USER (client)",
            tags = "Gestion des Commandes et Précommande"


    )
    public String lienUser(@PathVariable Long iduser, @PathVariable Long idproduit,@PathVariable int quantite){
        return utilisateurProduitCommandeService.lienUser(iduser,idproduit,quantite);
    }


    @PostMapping("/favori/{iduser}/{idproduit}")
    @Operation(
            summary = "Ajouter un produit en favoris",
            description = "*** Accès reservé aux USER (client)",
            tags = "Gestion des Commandes et Précommande"

    )
    public ResponseEntity<?> favori(@PathVariable Long iduser,@PathVariable Long idproduit){
        return utilisateurProduitCommandeService.favoris(iduser,idproduit);   
    }
    @DeleteMapping("/favori/remove/{iduser}/{idproduit}")
    @Operation(
            summary = "Retirer un produit en favoris",
            description = "*** Accès reservé aux USER (client)",
            tags = "Gestion des Commandes et Précommande"

    )
    public ResponseEntity<?> removefavori(@PathVariable Long iduser,@PathVariable Long idproduit){
        return utilisateurProduitCommandeService.removeProduitFavoris(iduser,idproduit);
    }




}
