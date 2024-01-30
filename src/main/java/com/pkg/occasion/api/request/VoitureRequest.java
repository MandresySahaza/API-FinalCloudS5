package com.pkg.occasion.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoitureRequest {
    public int id_categorie;
    public int id_marque;
    public int id_modele;
    public int id_energie;
    public int id_boitevitesse;
    public int id_etatvoiture;
    public double kilometrage;
    public String matricule;
}
