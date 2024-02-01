package com.pkg.occasion.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnonceRequestWithVoiture {
    public String voiture;
    public VoitureRequest voitureRequest;
    public String description;
    public double prix;
    public int status;

    public void setVoitureRequest(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            voitureRequest = objectMapper.readValue(voiture, VoitureRequest.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
