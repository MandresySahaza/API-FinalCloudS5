package com.pkg.occasion.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pkg.occasion.api.model.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur , Integer>{
    Optional<Utilisateur> findByMailAndPassword(String mail, String password);
    Optional<Utilisateur> findByMail(String mail);

    @Query("SELECT count(u) FROM Utilisateur u WHERE u.role = 'USER'")
    int nbUtilisateurs_User();

}
