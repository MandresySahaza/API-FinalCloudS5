package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pkg.occasion.api.model.Annonce;

public interface AnnonceRepository extends JpaRepository<Annonce , Integer>{
    @Query("SELECT a FROM Annonce a WHERE a.id_Voiture = :id_Voiture")
    List<Annonce> findById_voiture(@Param("id_Voiture") int id_voiture);

    @Query("SELECT a FROM Annonce a WHERE a.id_utilisateur = :id_utilisateur")
    List<Annonce> findMyAnnonces(@Param("id_utilisateur") int id_utilisateur);

    @Query("SELECT count(a) FROM Annonce a WHERE a.status = 20")
    int nbVoituresVendues();

    List<Annonce> findByStatus(int status);

    @Query(value = "SELECT * FROM Annonces WHERE id_Utilisateur != :id_Utilisateur AND status = 10" , nativeQuery = true)
    List<Annonce> findOffers(@Param("id_Utilisateur")int id_utilisateur);

    @Query(value = "SELECT * FROM Annonces WHERE status = 10" , nativeQuery = true)
    List<Annonce> findOffers();
}
