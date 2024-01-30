package com.pkg.occasion.api.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnonceRequestRech {
    public VoitureRequest voiture;
    public String mot_cle;
    public LocalDate dateDeb;
    public LocalDate dateFin;
    public double prixDeb;
    public double prixFin;
    public double kilometrageDeb;
    public double kilometrageFin;
    
}
