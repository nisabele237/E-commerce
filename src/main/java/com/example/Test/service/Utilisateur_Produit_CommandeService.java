package com.example.Test.service;

import com.example.Test.Exception.*;
import com.example.Test.model.Produit;
import com.example.Test.model.Role;
import com.example.Test.model.Utilisateur;
import com.example.Test.repository.ProduitRepository;
import com.example.Test.repository.UtilisateurRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Slf4j
@Service
public class Utilisateur_Produit_CommandeService {

    @Autowired
    private  UtilisateurRepository utilisateurRepository;
    @Autowired
    private  ProduitRepository produitRepository;
    @Autowired
    private EmailService emailService;



    //Creation et envoie du code
    public ResponseEntity<?> createutilisateur(Utilisateur user){
        //check
        if(user.getNom()==null || user.getRole()==null || user.getPassword()==null || user.getEmail()==null){
            throw  new BadRequestException("Information(s) Manquante(s)");
        }
        Utilisateur utilisateur = utilisateurRepository.findByPassword(user.getPassword()) ;
        if(utilisateur!=null){
            throw new InternalServorError("Mot de passe déja existant");
        }
        Utilisateur utilisateur1 = utilisateurRepository.findByEmail(user.getEmail()) ;
        if(utilisateur1 != null){
            throw new InternalServorError("Email déja existant déja existant");
        }
        //Envoie du code de verification
        Random random = new Random();
        //Code à 4 chiffres
        int min = 1000;
        int max = 9999;
        int code = random.nextInt((max - min) + 1) + min;
        user.setCode(String.valueOf(code));
       emailService.sendMail(user.getEmail(),"Code de Verification",user.getCode());

        user.statusverse();
        utilisateurRepository.save(Utilisateur.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .nom(user.getNom())
                        .code(user.getCode())
                        .montant(user.getMontant())
                        .produitFavoris(user.getProduitFavoris())
                        .produitList(user.getProduitList())
                        .role(user.getRole())
                        .password (user.getPassword())
                        .log(false)
                         .build());
        return ResponseEntity.ok(user);
    }

    //Verfication du code et Validation du compte
    public ResponseEntity<?> validUtilisateur(String email,String code){
         Utilisateur user = utilisateurRepository.findByEmail(email);
         if(user==null){
             throw new InternalServorError("Utilisateur introuvable");
         }
        if(code.equals(user.getCode())){
            user.setStatus(true);
            utilisateurRepository.save(user);
            return ResponseEntity.ok("Compte Authentifié avec Succès");
        }
        throw new ForbiddenException("Code Incorrecte Creation de compte impossible");

    }

    //log in
    public ResponseEntity<?> logInUser(String email, String password){
        // 1. Verifier si les coordonnées existent bien
        Utilisateur utilisateur = utilisateurRepository.findByEmailAndPassword(email,password);

        if(utilisateur!=null){
            if(utilisateur.isStatus()){
                utilisateur.setLog(true);
                utilisateurRepository.save(utilisateur);
                return ResponseEntity.ok("Connexion effectuée avec succès!");
            }
            throw new InternalServorError("Impossible d'etablir une connexion. Compte inexistant !");
        }
        throw new EntityNotFoundException("Utilisateur introuvable");
    }

    //log out
    public ResponseEntity<?> logOutUser(String email){
        Utilisateur user = utilisateurRepository.findByEmail(email);
        if(user!=null){
            user.logverse();
            utilisateurRepository.save(user);
            return ResponseEntity.ok("Déconnexion effectuée avec succès!");
        }
            throw new InternalServorError("Utilisateur introuvable");

    }
    public Produit createproduit(Produit produit){
        if(produit.getDesignation()==null|| produit.getQuantite()== 0){
            throw new BadRequestException("Information(s) Manquantes(s)");
        }
        return produitRepository.save(new Produit(produit.getDesignation(),produit.getQuantite(),produit.getPrixunitaire(),produit.getCategorie()));
    }


    public ResponseEntity<?> findAllUser(){
        List<Utilisateur> utilisateurs = utilisateurRepository.findByStatus(true);
        return ResponseEntity.ok(utilisateurs);
    }
    public List<Produit> findAllProduit(){
        return produitRepository.findByStatus(true);
    }



    //Commander un produit avec quantité
    public String lienUser (Long iduser, Long idproduit,int quantite){
        Produit produit = produitRepository.findByIdAndStatus(idproduit,true);
        Utilisateur user = utilisateurRepository.findByIdAndLog(iduser,true);
     if(user!=null) {
         if (user.getRole() == Role.USER) {
             int depense = (quantite * produit.getPrixunitaire());
             //Mise à jour de la quantité
             produit.setQuantite(produit.getQuantite() - quantite);
             produitRepository.save(produit);
             //MiSE À Jour du montant de la depense
             user.setMontant(user.getMontant() + depense);
             if (!user.getProduitList().contains(produit)) {
                 //Ajout du produit à la liste de l'utilisateur
                 user.getProduitList().add(produit);
                 //Ajout de l'utilisateur  à la liste du produit
                 produit.getUtilisateurList().add(user);
             }
             produit.setNombrecommande(produit.getNombrecommande() + 1);
             //retrait du produit de la liste de favoris
             user.getProduitFavoris().remove(produit);
             //Sauvegarde
             produitRepository.save(produit);
             utilisateurRepository.save(user);


             return "Ajout effectué avec succès Solde du produit :  " + depense + " Solde total: " + user.getMontant();
         } else {
             return "Violation des droits ! Seuls les Users peuvent ajouter un produit";
         }
     }
     else{
         throw new InternalServorError("Connexion à un compte requise");
     }
    }

    //Restocker un Produit avec quantité
    public ResponseEntity<?> lienAdmin(Long idadmin, Long idproduit,int quantite) {
        Produit produit = produitRepository.findByIdAndStatus(idproduit,true);
        Utilisateur user = utilisateurRepository.findByIdAndLog(idadmin,true);
        if(user!=null) {
            if (user.getRole() == Role.ADMIN) {
                //Mise à jour de la quantité
                produit.setQuantite(produit.getQuantite() + quantite);
                if (!user.getProduitList().contains(produit)) {
                    //Ajout du produit à la liste de l'admin
                    user.getProduitList().add(produit);
                    //Ajout de l'admin  à la liste du produit
                    produit.getUtilisateurList().add(user);
                    //Sauvegarde
                }
                produitRepository.save(produit);
                utilisateurRepository.save(user);
                return ResponseEntity.ok(produit);
            } else {
                throw new ForbiddenException("Violation des droits! Seuls les admins peuvent effectuer cette operation");
            }
        }else{
            throw new InternalServorError("Connexion à un compte requise");
        }
    }
//Ajouter un produit à la liste des favoris

    public ResponseEntity<?> favoris (Long iduser, Long idproduit){
        Produit produit = produitRepository.findByIdAndStatus(idproduit,true);
        Utilisateur user = utilisateurRepository.findByIdAndLog(iduser,true);
        if(user!=null) {
            if (user.getProduitFavoris().contains(produit)) {
                throw new EntityAlreadyExistException("Produit déja existant dans la liste");
            }
            user.getProduitFavoris().add(produit);
            //sauvegarde
            utilisateurRepository.save(user);
            return ResponseEntity.ok(user);
        }else{
            throw new InternalServorError("Connexion à un compte requise");
        }
    }



 //supprimer un produit

 public ResponseEntity<?> DeleteProduit(Long idadmin, Long idproduit){
     Produit produit = produitRepository.findByIdAndStatus(idproduit, true);
     Utilisateur user = utilisateurRepository.findByIdAndLog(idadmin,true);
     if(user!=null) {
         if (user.getRole() == Role.ADMIN) {
             if (user.getProduitList().contains(produit)) {
                 //retrait du produit à la liste de l'admin
                 user.getProduitList().remove(produit);
             }
             //suppression
             produit.setStatus(!produit.isStatus());
             //Sauvegarde
             produitRepository.save(produit);
             utilisateurRepository.save(user);

             return ResponseEntity.ok("Suppression effectué avec succes");


         } else {
             throw new ForbiddenException("Accès interdit");
         }
     }else{
         throw new InternalServorError("Connexion à un compte requise");

     }

 }

 //Obtenir un produit par Id
    public ResponseEntity<?> findProductById( Long idproduit){
       Produit produit = produitRepository.findByIdAndStatus(idproduit,true);
       if(produit  == null){
           throw new EntityNotFoundException("Produit introuvable");

       }
        return ResponseEntity.ok(produit);
    }

    //Obtenir la liste de produit par categorie
    public ResponseEntity<?> findProduitByCategorie(String categorie){
        List<Produit> produits = produitRepository.findByCategorieAndStatus(categorie,true);

        if(produits == null){
            throw new EntityNotFoundException("Categorie introuvable ou vide");
        }
        return ResponseEntity.ok(produits);
    }

    //Supprimer un produit de la liste des favoris
    public ResponseEntity<?>removeProduitFavoris (Long iduser, Long idproduit){
        Utilisateur user = utilisateurRepository.findById(iduser).orElseThrow(()->new EntityNotFoundException("Utilisateur introuvable"));
        Produit produit = produitRepository.findByIdAndStatus(idproduit,true);

        if(user.getRole()==Role.USER){
            if(user.getProduitFavoris().contains(produit)){
                user.getProduitFavoris().remove(produit);
                utilisateurRepository.save(user);
                return ResponseEntity.ok(user);
            }else{
                throw new EntityNotFoundException("Produit introuvable");
            }

        }
        throw new ForbiddenException("Accès interdit");
    }

    //Modification d'un produit
    public ResponseEntity<?> updateProduct(Produit produit, Long idproduit){
        if(!produitRepository.existsById(idproduit) && produit.isStatus()){
             throw new EntityNotFoundException("Produit introuvable");
        }
        produit.setId(idproduit);
        produitRepository.save(produit);
        return ResponseEntity.ok(produit);
    }

    //Consulter l'ensemble des produits marqués comme payant
    public ResponseEntity<?> consultProduit(Long iduser){
        Utilisateur user = utilisateurRepository.findById(iduser).orElseThrow(()->new EntityNotFoundException("Utilisateur introuvable"));
        if(user.getRole()==Role.USER){
            List<Produit> produits = produitRepository.findByNombrecommandeGreaterThan(1);
            return ResponseEntity.ok(produits);
        }
        throw new ForbiddenException("Accès interdit");



    }

}
