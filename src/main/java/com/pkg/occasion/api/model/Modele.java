package com.pkg.occasion.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name="modeles")
public class Modele {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modele")
    public int id;
    public String nom;
    @Column(name = "anneesortie")
    public int anneeSortie;

    @ManyToOne
    @JoinColumn(name="id_categorie")
    public Categorie categorie;

    @JsonIgnore
    @Column(name = "id_marque" , insertable = false , updatable = false)
    public int id_marque;

    @ManyToOne
    @JoinColumn(name="id_marque")
    public Marque marque;
}
