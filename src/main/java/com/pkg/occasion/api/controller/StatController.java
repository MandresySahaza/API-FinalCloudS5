package com.pkg.occasion.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pkg.occasion.api.model.Stat;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.StatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class StatController {
    @Autowired
    private StatService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Format> getAll() {
        Stat stat = Stat.builder()
            .nbVoitureVendues(service.nbVoituresVendues())
            .nbUtilisateurs(service.nbUtilisateurs_User())
            .totalBenefice(service.totalBenefice())
            .totalChiffreAffaire(service.totalChiffreAffaire())

            .usersParMois(service.inscriptionParMois())
            .annonceParMois(service.annonceParMois())
            .build();

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(stat)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
}
