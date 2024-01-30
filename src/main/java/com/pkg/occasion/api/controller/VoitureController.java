package com.pkg.occasion.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pkg.occasion.api.model.BoiteVitesse;
import com.pkg.occasion.api.model.Categorie;
import com.pkg.occasion.api.model.Energie;
import com.pkg.occasion.api.model.EtatVoiture;
import com.pkg.occasion.api.model.Marque;
import com.pkg.occasion.api.model.Modele;
import com.pkg.occasion.api.model.Voiture;
import com.pkg.occasion.api.request.VoitureRequest;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.BoiteVitesseService;
import com.pkg.occasion.api.service.CategorieService;
import com.pkg.occasion.api.service.CounterService;
import com.pkg.occasion.api.service.EnergieService;
import com.pkg.occasion.api.service.EtatVoitureService;
import com.pkg.occasion.api.service.MarqueService;
import com.pkg.occasion.api.service.ModeleService;
import com.pkg.occasion.api.service.VoitureService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/voitures")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class VoitureController {
    @Autowired
    private VoitureService service;

    @Autowired
    private CounterService counterService;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private MarqueService marqueService;

    @Autowired
    private ModeleService modeleService;

    @Autowired
    private EnergieService energieService;

    @Autowired
    private BoiteVitesseService boitevitesseService;

    @Autowired
    private EtatVoitureService etatvoitureService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Format> getAll() {
        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.findAll())
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Format> getById(@PathVariable int id) {
        if(!service.existsById(id)){
            Format format = Format.builder() 
                .code(10)
                .message("Cette voiture n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

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
    public ResponseEntity<Format> create(@RequestBody VoitureRequest request) {

        var modele_list = service.findByMatricule(request.getMatricule());

        if(modele_list.size() != 0){
            Format format = Format.builder()
                .code(10)
                .message("Une voiture avec ce matricule existe déja")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        // Debut Check existence 

        Categorie categorie = categorieService.findById(request.getId_categorie());

        if(categorie == null){
            Format format = Format.builder()
                .code(20)
                .message("Cette categorie n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Marque marque = marqueService.findById(request.getId_marque());

        if(marque == null){
            Format format = Format.builder()
                .code(30)
                .message("Cette marque n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Modele modele = modeleService.findById(request.getId_modele());

        if(modele == null){
            Format format = Format.builder()
                .code(30)
                .message("Ce modele n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }else if(modele.getMarque().getId() != marque.getId() || modele.getCategorie().getId() != categorie.getId()){
            Format format = Format.builder()
                .code(30)
                .message("Ce modele n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Energie energie = energieService.findById(request.getId_energie());

        if(energie == null){
            Format format = Format.builder()
                .code(40)
                .message("Cet energie n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        BoiteVitesse boite = boitevitesseService.findById(request.getId_boitevitesse());

        if(boite == null){
            Format format = Format.builder()
                .code(50)
                .message("Cette boite de vitesse n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        EtatVoiture etat = etatvoitureService.findById(request.getId_etatvoiture());

        if(etat == null){
            Format format = Format.builder()
                .code(60)
                .message("Cet etat voiture n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        // Fin Check existence 




        Voiture nv_voiture = Voiture.builder()
            .id(counterService.getNextSequence(Voiture.SEQUENCE_NAME))
            .id_categorie(request.getId_categorie())
            .id_marque(request.getId_marque())
            .id_modele(request.getId_modele())
            .id_energie(request.getId_energie())
            .id_boitevitesse(request.getId_boitevitesse())
            .id_etatvoiture(request.getId_etatvoiture())

            .categorie(categorie)
            .marque(marque)
            .modele(modele)
            .energie(energie)
            .boite(boite)
            .etatVoiture(etat)

            .kilometrage(request.getKilometrage())
            .matricule(request.getMatricule())
            .build();


        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.save(nv_voiture))
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Format> update(@PathVariable int id, @RequestBody VoitureRequest request) {
        
        if(!service.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        var other = service.findByMatricule(request.getMatricule());

        if(other.size() != 0){
            if(other.get(0).getId() != id){
                Format format = Format.builder() 
                    .code(10)
                    .message("Une voiture avec ce matricule existe déja")
                    .result(null)
                    .time(System.currentTimeMillis())
                    .build();
                return ResponseEntity.ok(format);
            }
        }

        // Debut Check existence 

        Categorie categorie = categorieService.findById(request.getId_categorie());

        if(categorie == null){
            Format format = Format.builder()
                .code(20)
                .message("Cette categorie n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Marque marque = marqueService.findById(request.getId_marque());

        if(marque == null){
            Format format = Format.builder()
                .code(30)
                .message("Cette marque n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Modele modele = modeleService.findById(request.getId_modele());

        if(modele == null){
            Format format = Format.builder()
                .code(30)
                .message("Ce modele n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }else if(modele.getMarque().getId() != marque.getId() || modele.getCategorie().getId() != categorie.getId()){
            Format format = Format.builder()
                .code(30)
                .message("Ce modele n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Energie energie = energieService.findById(request.getId_energie());

        if(energie == null){
            Format format = Format.builder()
                .code(40)
                .message("Cet energie n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        BoiteVitesse boite = boitevitesseService.findById(request.getId_boitevitesse());

        if(boite == null){
            Format format = Format.builder()
                .code(50)
                .message("Cette boite de vitesse n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        EtatVoiture etat = etatvoitureService.findById(request.getId_etatvoiture());

        if(etat == null){
            Format format = Format.builder()
                .code(60)
                .message("Cet etat voiture n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        // Fin Check existence 


        Voiture voiture = Voiture.builder()
            .id(id)
            .id_categorie(request.getId_categorie())
            .id_marque(request.getId_marque())
            .id_modele(request.getId_modele())
            .id_energie(request.getId_energie())
            .id_boitevitesse(request.getId_boitevitesse())
            .id_etatvoiture(request.getId_etatvoiture())

            .categorie(categorie)
            .marque(marque)
            .modele(modele)
            .energie(energie)
            .boite(boite)
            .etatVoiture(etat)

            .kilometrage(request.getKilometrage())
            .matricule(request.getMatricule())
            .build();


        Format format = Format.builder() 
            .code(0)
            .message("OK")
            .result(service.save(voiture))
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
