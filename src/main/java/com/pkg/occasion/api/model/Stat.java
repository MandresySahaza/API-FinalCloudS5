package com.pkg.occasion.api.model;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Stat {
    public int nbVoitureVendues;
    public int nbUtilisateurs;
    public BigDecimal totalBenefice;
    public BigDecimal totalChiffreAffaire;
    public ChartStat usersParMois;  
    public ChartStat annonceParMois;  
}
