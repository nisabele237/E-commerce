package com.example.Test.repository;


import com.example.Test.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

    Utilisateur findByPassword(String password);

    List<Utilisateur> findByStatus(boolean b);

    Utilisateur findByEmail(String email);

    Utilisateur findByNom(String email);

    Utilisateur findByEmailAndPassword(String email, String password);

    Utilisateur findByIdAndLog(Long id, boolean log);
}
