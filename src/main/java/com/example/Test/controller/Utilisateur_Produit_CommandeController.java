package com.example.Test.controller;

import com.example.Test.model.Produit;
import com.example.Test.model.Utilisateur;
import com.example.Test.service.Utilisateur_Produit_CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class Utilisateur_Produit_CommandeController
{
    @Autowired
    private  Utilisateur_Produit_CommandeService utilisateurProduitCommandeService;

    @PostMapping("/compte/user")
    public ResponseEntity<?> createutilisateur(@RequestBody Utilisateur user){
        return utilisateurProduitCommandeService.createutilisateur(user);
    }
    @PostMapping("/validation/user/{email}/{code}")
    public ResponseEntity<?> validutilisateur(@PathVariable String email,@PathVariable String code){
        return utilisateurProduitCommandeService.validUtilisateur(email,code);
    }
    @PostMapping("/login/user/{email}/{password}")
    public ResponseEntity<?> loginutilisateur(@PathVariable String email,@PathVariable String password){
        return utilisateurProduitCommandeService.logInUser(email,password);
    }
    @PostMapping("/logout/user/{email}")
    public ResponseEntity<?> logoututilisateur(@PathVariable String email){
        return utilisateurProduitCommandeService.logOutUser(email);
    }

    @PostMapping("/produit")
    public Produit createproduit( @RequestBody Produit produit){
        return utilisateurProduitCommandeService.createproduit(produit);
    }


    @GetMapping("/user")
    public ResponseEntity<?> getallutilisateur(){
        return utilisateurProduitCommandeService.findAllUser();
    }
    @GetMapping("/produit")
    public List<Produit>getallproduit(){
        return utilisateurProduitCommandeService.findAllProduit();
    }


    @PostMapping("/user/{iduser}/{idproduit}/{quantite}")
    public String lienUser(@PathVariable Long iduser, @PathVariable Long idproduit,@PathVariable int quantite){
        return utilisateurProduitCommandeService.lienUser(iduser,idproduit,quantite);
    }

    @PostMapping ("/restock/{iduser}/{idproduit}/{quantite}")
    public ResponseEntity<?> lienAdmin(@PathVariable(name = "iduser") Long idadmin,@PathVariable  Long idproduit,@PathVariable int quantite){
        return utilisateurProduitCommandeService.lienAdmin(idadmin,idproduit,quantite);
    }
    @DeleteMapping ("/destock/{iduser}/{idproduit}")
    public ResponseEntity<?> lienAdmin(@PathVariable(name = "iduser") Long idadmin, @PathVariable  Long idproduit){
        return utilisateurProduitCommandeService.DeleteProduit(idadmin,idproduit);
    }

    @PostMapping("/favori/{iduser}/{idproduit}")
    public ResponseEntity<?> favori(@PathVariable Long iduser,@PathVariable Long idproduit){
        return utilisateurProduitCommandeService.favoris(iduser,idproduit);   
    }
    @DeleteMapping("/favori/remove/{iduser}/{idproduit}")
    public ResponseEntity<?> removefavori(@PathVariable Long iduser,@PathVariable Long idproduit){
        return utilisateurProduitCommandeService.removeProduitFavoris(iduser,idproduit);
    }
    @GetMapping ("/produit/{idproduit}")
    public ResponseEntity<?> findProduit(@PathVariable (name = "idproduit") Long id){
        return utilisateurProduitCommandeService.findProductById(id);
    }
    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<?> findByCategorie(@PathVariable String categorie){
        return utilisateurProduitCommandeService.findProduitByCategorie(categorie);
    }
    @PostMapping("/delete/{iduser}/{idproduit}")
    public ResponseEntity<?> DeleteProduct(@PathVariable Long iduser,@PathVariable Long idproduit){
        return utilisateurProduitCommandeService.removeProduitFavoris(iduser,idproduit);
    }
    @PutMapping("update/{idproduit}")
    public ResponseEntity<?> updateProduct(@RequestBody Produit produit,@PathVariable Long idproduit){
        return utilisateurProduitCommandeService.updateProduct(produit,idproduit);
    }
    @GetMapping("consult/{iduser}")
        public ResponseEntity<?> consultProduct (@PathVariable Long iduser){
            return utilisateurProduitCommandeService.consultProduit(iduser);
        }


}
