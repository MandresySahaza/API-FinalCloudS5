package com.pkg.occasion.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnonceRequestWithVoiture {
    public VoitureRequest voiture;
    public String description;
    public double prix;
    public int status;
}
