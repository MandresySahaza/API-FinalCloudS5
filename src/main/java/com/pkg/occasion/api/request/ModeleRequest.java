package com.pkg.occasion.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModeleRequest {
    String nom;
    int anneeSortie;
    int id_categorie;
    int id_marque;
}
