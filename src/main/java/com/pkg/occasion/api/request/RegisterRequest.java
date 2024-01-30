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
public class RegisterRequest {
    private String nom ;
    private String prenom ;
    private LocalDate dateNaissance ;
    private String password ;
    private String mail ;
    private String contact ;
    private String adresse ;
    private String cin ;
}
