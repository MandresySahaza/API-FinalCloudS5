package com.pkg.occasion.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pkg.occasion.api.model.Utilisateur;
import com.pkg.occasion.user.Role;
import java.util.List;


public interface UtilisateurRepository extends JpaRepository<Utilisateur , Integer>{
    Optional<Utilisateur> findByMailAndPassword(String mail, String password);
    Optional<Utilisateur> findByMail(String mail);

    @Query("SELECT count(u) FROM Utilisateur u WHERE u.role = 'USER'")
    int nbUtilisateurs_User();

    @Query("SELECT u from Utilisateur u WHERE u.role = 'USER'")
    List<Utilisateur> getAllUsers();

}
