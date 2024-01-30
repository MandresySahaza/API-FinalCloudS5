package com.pkg.occasion.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pkg.occasion.api.model.Annonce;
import com.pkg.occasion.api.model.Commission;
import com.pkg.occasion.api.request.CommissionRequest;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.AnnonceService;
import com.pkg.occasion.api.service.CommissionService;
import com.pkg.occasion.api.service.UtilisateurService;
import com.pkg.occasion.api.service.VoitureService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/commissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class CommissionController {
    @Autowired
    private CommissionService service;

    @Autowired
    private AnnonceService annonceService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private VoitureService voitureService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Format> getAll() {
        List<Commission> commissions = service.findAll();

        for(Commission commission : commissions){
            commission.setAnnonce_spring(annonceService, utilisateurService, voitureService);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(commissions)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Format> getById(@PathVariable int id , Authentication auth) {
        if(!service.existsById(id)){
            Format format = Format.builder() 
                .code(10)
                .message("Cette commission n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Commission commission = service.findById(id);
        commission.setAnnonce_spring(annonceService, utilisateurService, voitureService);

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.findById(id))
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<Format> create(@RequestBody CommissionRequest request) {

        var modele = service.findById_Annonce(request.getId_annonce());

        if(modele.size() != 0){
            Format format = Format.builder()
                .code(10)
                .message("Une commission a cet annonce existe déja")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        if(request.getValeur() <= 0){
            Format format = Format.builder()
                .code(100)
                .message("Prix invalide")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Annonce annonce = annonceService.findById(request.getId_annonce());

        if(annonce == null){
            Format format = Format.builder()
                .code(20)
                .message("Cet annonce n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }else if(annonce.enAttente() == false){
            Format format = Format.builder()
                .code(30)
                .message("Cet annonce n'est pas en attente")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        annonce.setUserVoiture(utilisateurService, voitureService);


        Commission nv_commission = Commission.builder()
            .valeur(request.getValeur())
            .id_Annonce(request.getId_annonce())
            .annonce(annonce)
            .build();


        service.save(nv_commission);

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(nv_commission)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Format> update(@PathVariable int id, @RequestBody CommissionRequest request) {
        
        if(!service.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        var other = service.findById_Annonce(request.getId_annonce());

        if(other.size() != 0){
            if(other.get(0).getId() != id){
                Format format = Format.builder() 
                    .code(10)
                    .message("Une commission a cet annonce existe deja")
                    .result(null)
                    .time(System.currentTimeMillis())
                    .build();
                return ResponseEntity.ok(format);
            }
        }

        Annonce annonce = annonceService.findById(request.getId_annonce());

        if(annonce == null){
            Format format = Format.builder()
                .code(20)
                .message("Cet annonce n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        annonce.setUserVoiture(utilisateurService, voitureService);


        Commission nv_commission = Commission.builder()
            .id(id)
            .valeur(request.getValeur())
            .id_Annonce(request.getId_annonce())
            .annonce(annonce)
            .build();

        service.save(nv_commission);

        Format format = Format.builder() 
            .code(0)
            .message("OK")
            .result(nv_commission)
            .time(System.currentTimeMillis())
            .build();

        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        // return entity;
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
