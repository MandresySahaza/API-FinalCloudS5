package com.pkg.occasion.api.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.ChartStat;
import com.pkg.occasion.api.repository.AnnonceCountRepository;
import com.pkg.occasion.api.repository.AnnonceRepository;
import com.pkg.occasion.api.repository.CommissionRepository;
import com.pkg.occasion.api.repository.MonthCountRepository;
import com.pkg.occasion.api.repository.UtilisateurRepository;

@Service
public class StatService {
    @Autowired
    private AnnonceRepository annonceRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private MonthCountRepository monthCountRepository;

    @Autowired
    private AnnonceCountRepository annonceCountRepository;

    public int nbVoituresVendues(){
        return annonceRepository.nbVoituresVendues();
    }

    public int nbUtilisateurs_User(){
        return utilisateurRepository.nbUtilisateurs_User();
    }

    public BigDecimal totalBenefice(){
        return commissionRepository.totalBenefice();
    }

    public BigDecimal totalChiffreAffaire(){
        return commissionRepository.totalChiffreAffaire();
    }

    public ChartStat inscriptionParMois(){
        return new ChartStat(monthCountRepository.findAll());
    }

    public ChartStat annonceParMois(){
        return new ChartStat(annonceCountRepository.findAll() , "");
    }
}
