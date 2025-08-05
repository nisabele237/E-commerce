package com.example.Test.repository;

import com.example.Test.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit,Long> {
    


    List<Produit> findByCategorieAndStatus(String categorie, boolean b);

    Produit  findByIdAndStatus(Long idproduit, boolean b);

    List<Produit> findByStatus(boolean b);


    List<Produit> findByNombrecommandeGreaterThan(int i);
}
