package com.pkg.occasion.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "voitures")
public class Voiture {
    @Transient
    public static final String SEQUENCE_NAME = "voitures_sequence";

    @Id
    @Column(name="id_voiture")
    public int id;
    @JsonIgnore
    public int id_categorie;
    @JsonIgnore
    public int id_marque;
    @JsonIgnore
    public int id_modele;
    @JsonIgnore
    public int id_energie;
    @JsonIgnore
    public int id_boitevitesse;
    @JsonIgnore
    public int id_etatvoiture;

    @Transient
    public Categorie categorie;
    @Transient
    public Marque marque;
    @Transient
    public Modele modele;
    @Transient
    public Energie energie;
    @Transient
    public BoiteVitesse boite;
    @Transient
    public EtatVoiture etatVoiture;

    public double kilometrage;
    public String matricule;
}
