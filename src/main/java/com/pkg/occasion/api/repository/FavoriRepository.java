package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pkg.occasion.api.model.Favori;

public interface FavoriRepository extends JpaRepository<Favori , Integer>{
    @Query("SELECT f FROM Favori f WHERE f.id_Utilisateur = :id_Utilisateur AND f.id_Annonce = :id_Annonce")
    public List<Favori> findById_utilisateurAndId_annonce(@Param("id_Utilisateur") int id_utilisateur , @Param("id_Annonce") int id_annonce);

    @Query("SELECT f FROM Favori f WHERE f.id_Utilisateur = :id_Utilisateur")
    public List<Favori> findMyFavourites(@Param("id_Utilisateur") int id_utilisateur);

}
