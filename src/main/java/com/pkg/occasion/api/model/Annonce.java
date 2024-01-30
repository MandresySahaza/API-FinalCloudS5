package com.pkg.occasion.api.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pkg.occasion.api.service.PhotoService;
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
@Table(name="annonces")
public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_annonce")
    public int id;

    @JsonIgnore
    public int id_utilisateur;

    @Column(name = "id_Voiture")
    public int id_Voiture;

    @Column(name = "datepub")
    public LocalDate datePub;

    public double prix;

    public int status;

    public String description;

    @Transient
    public List<Photo> photos;


    @Transient
    public UtilisateurMasque utilisateur;

    @Transient
    public Voiture voiture; 
    
    public void setUserVoiture(UtilisateurService utilisateurService , VoitureService voitureService){
        setUtilisateur(utilisateurService.findById(id_utilisateur).masquer());
        setVoiture(voitureService.findById(id_Voiture));
    }

    public boolean enAttente(){
        return status == 0;
    }

    public boolean enCours(){
        return status == 10;
    }

    public boolean vendu(){
        return status == 20;
    }

    public void setPhotos_base(PhotoService service){
        setPhotos(service.findById_Annonce(id));
    }
}
