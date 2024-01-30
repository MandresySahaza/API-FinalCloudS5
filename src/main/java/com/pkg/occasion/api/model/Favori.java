package com.pkg.occasion.api.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pkg.occasion.api.service.AnnonceService;
import com.pkg.occasion.api.service.UtilisateurService;
import com.pkg.occasion.api.service.VoitureService;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name="favoris")
public class Favori {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favori")
    public int id;

    @Column(name = "dateajout")
    public LocalDate dateAjout;

    @JsonIgnore
    @Column(name = "id_utilisateur")
    public int id_Utilisateur;

    @JsonIgnore
    @Column(name = "id_annonce")
    public int id_Annonce;
    
    @Transient
    public Utilisateur utilisateur;

    @Transient
    public Annonce annonce;

    public void setUtilisateurAnnonce(UtilisateurService utilisateurService , AnnonceService annonceService , VoitureService voitureService){
        setUtilisateur(utilisateurService.findById(id_Utilisateur));
        Annonce annonce = annonceService.findById(id_Annonce);
        annonce.setUserVoiture(utilisateurService, voitureService);
        setAnnonce(annonce);
    }

}
