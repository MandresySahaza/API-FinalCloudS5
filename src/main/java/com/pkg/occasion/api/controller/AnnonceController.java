package com.pkg.occasion.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

import com.pkg.occasion.api.model.Annonce;
import com.pkg.occasion.api.model.BoiteVitesse;
import com.pkg.occasion.api.model.Categorie;
import com.pkg.occasion.api.model.Energie;
import com.pkg.occasion.api.model.EtatVoiture;
import com.pkg.occasion.api.model.Marque;
import com.pkg.occasion.api.model.Modele;
import com.pkg.occasion.api.model.Photo;
import com.pkg.occasion.api.model.Voiture;
import com.pkg.occasion.api.request.AnnonceRequest;
import com.pkg.occasion.api.request.AnnonceRequestRech;
import com.pkg.occasion.api.request.AnnonceRequestWithStatus;
import com.pkg.occasion.api.request.AnnonceRequestWithVoiture;
import com.pkg.occasion.api.request.AnnonceRequestRech;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.AnnonceService;
import com.pkg.occasion.api.service.BoiteVitesseService;
import com.pkg.occasion.api.service.CategorieService;
import com.pkg.occasion.api.service.CounterService;
import com.pkg.occasion.api.service.EnergieService;
import com.pkg.occasion.api.service.EtatVoitureService;
import com.pkg.occasion.api.service.MarqueService;
import com.pkg.occasion.api.service.ModeleService;
import com.pkg.occasion.api.service.PhotoService;
import com.pkg.occasion.api.service.UtilisateurService;
import com.pkg.occasion.api.service.VoitureService;
import com.pkg.occasion.util.Util;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/annonces")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class AnnonceController {
    @Autowired
    private AnnonceService service;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private VoitureService voitureService;

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

    @Autowired
    private PhotoService photoService;


    @GetMapping
    public ResponseEntity<Format> getAll() {
        List<Annonce> annonces = service.findAll();
        for (Annonce annonce : annonces) {
            annonce.setUtilisateur(utilisateurService.findById(annonce.getId_utilisateur()).masquer());
            annonce.setVoiture(voitureService.findById(annonce.getId_Voiture())); 
            annonce.setPhotos_base(photoService);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(annonces)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Format> getById(@PathVariable int id) {
        if(!service.existsById(id)){
            Format format = Format.builder() 
                .code(10)
                .message("Cet annonce n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Annonce annonce = service.findById(id);
        annonce.setUtilisateur(utilisateurService.findById(annonce.getId_utilisateur()).masquer());
        annonce.setVoiture(voitureService.findById(annonce.getId_Voiture())); 

        annonce.setPhotos_base(photoService);

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(annonce)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<Format> create(@RequestBody MultipartFile[] files , AnnonceRequest request , Authentication auth) {

        var annonce = service.findById_voiture(request.getId_voiture());
        if(annonce.size() != 0){
            Format format = Format.builder()
                .code(10)
                .message("Une annonce avec cette voiture existe déja")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        if(request.getPrix() <= 0){
            Format format = Format.builder()
                .code(100)
                .message("Prix invalide")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        Voiture voiture = voitureService.findById(request.getId_voiture());

        if(voiture == null){
            Format format = Format.builder()
                .code(20)
                .message("Cette voiture n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        var user = utilisateurService.findByMail(auth.getName());
        

        Annonce nv_annonce = Annonce.builder()
            .id_utilisateur(user.getId())
            .id_Voiture(request.getId_voiture())
            .datePub(LocalDate.now())
            .prix(request.getPrix())
            .status(0)
            .description(request.getDescription())

            .voiture(voiture)
            .utilisateur(user.masquer())
            .build();

        Annonce apres = service.save(nv_annonce);
        

        for (MultipartFile file : files) {
            try{
                String downloadUrl = Util.uploadFile(file);

                Photo photo = Photo.builder()
                    .id(counterService.getNextSequence(Photo.SEQUENCE_NAME))
                    .id_annonce(apres.getId())
                    .lien(downloadUrl)
                    .build();

                photoService.save(photo);
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
        apres.setPhotos_base(photoService);
        

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(apres)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
    
    @PreAuthorize("hasRole('ADMIN') or @utilisateurservice.isMyAnnonce(#id , authentication.principal.username) == true")
    @PutMapping("/{id}")
    public ResponseEntity<Format> update(@PathVariable int id, @RequestBody AnnonceRequestWithStatus request , Authentication auth) {
        
        if(!service.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        var other = service.findById_voiture(request.getId_voiture());

        if(other.size() != 0){
            if(other.get(0).getId() != id){
                Format format = Format.builder() 
                    .code(10)
                    .message("Une annonce avec cette voiture existe déjà")
                    .result(null)
                    .time(System.currentTimeMillis())
                    .build();
                return ResponseEntity.ok(format);
            }
        }

        if(request.getPrix() <= 0){
            Format format = Format.builder()
                .code(100)
                .message("Prix invalide")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Voiture voiture = voitureService.findById(request.getId_voiture());

        if(voiture == null){
            Format format = Format.builder()
                .code(20)
                .message("Cette voiture n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        var user = utilisateurService.findByMail(auth.getName());
        

        Annonce nv_annonce = Annonce.builder()
            .id(id)
            .id_utilisateur(user.getId())
            .id_Voiture(request.getId_voiture())
            .datePub(LocalDate.now())
            .prix(request.getPrix())
            .status(request.getStatus())
            .description(request.getDescription())

            .voiture(voiture)
            .utilisateur(user.masquer())
            .build();

        Annonce apres = service.save(nv_annonce);
        apres.setPhotos_base(photoService);


        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(apres)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('ADMIN') or @utilisateurservice.isMyAnnonce(#id , authentication.principal.username) == true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        // return entity;
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================================================================================
    // FIN REST methodes
    // ================================================================================

    @PreAuthorize("hasRole('USER')")
    @GetMapping("myannonces")
    public ResponseEntity<Format> getMyAnnonces(@RequestBody MultipartFile[] files , Authentication auth) {
        var user = utilisateurService.findByMail(auth.getName());

        List<Annonce> annonces = service.findMyAnnonces(user.getId());
        for (Annonce annonce : annonces) {
            annonce.setUtilisateur(utilisateurService.findById(annonce.getId_utilisateur()).masquer());
            annonce.setVoiture(voitureService.findById(annonce.getId_Voiture())); 

            annonce.setPhotos_base(photoService);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(annonces)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }



    @PreAuthorize("hasRole('USER')")
    @PostMapping("/voiture")
    public ResponseEntity<Format> createWithVoiture(@RequestBody MultipartFile[] files , AnnonceRequestWithVoiture request , Authentication auth) {
        request.setVoitureRequest();

        if(voitureService.existsByMatricule(request.getVoitureRequest().getMatricule()) == true){
            Format format = Format.builder()
                .code(10)
                .message("La voiture entré existe deja")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        if(request.getPrix() <= 0){
            Format format = Format.builder()
                .code(100)
                .message("Prix invalide")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        // Debut Check existence 

        Categorie categorie = categorieService.findById(request.getVoitureRequest().getId_categorie());

        if(categorie == null){
            Format format = Format.builder()
                .code(20)
                .message("Cette categorie n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Marque marque = marqueService.findById(request.getVoitureRequest().getId_marque());

        if(marque == null){
            Format format = Format.builder()
                .code(30)
                .message("Cette marque n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Modele modele = modeleService.findById(request.getVoitureRequest().getId_modele());

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

        Energie energie = energieService.findById(request.getVoitureRequest().getId_energie());

        if(energie == null){
            Format format = Format.builder()
                .code(40)
                .message("Cet energie n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        BoiteVitesse boite = boitevitesseService.findById(request.getVoitureRequest().getId_boitevitesse());

        if(boite == null){
            Format format = Format.builder()
                .code(50)
                .message("Cette boite de vitesse n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }


        EtatVoiture etat = etatvoitureService.findById(request.getVoitureRequest().getId_etatvoiture());

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
            .id(counterService.getNextSequence(Voiture.SEQUENCE_NAME))
            .id_categorie(request.getVoitureRequest().getId_categorie())
            .id_marque(request.getVoitureRequest().getId_marque())
            .id_modele(request.getVoitureRequest().getId_modele())
            .id_energie(request.getVoitureRequest().getId_energie())
            .id_boitevitesse(request.getVoitureRequest().getId_boitevitesse())
            .id_etatvoiture(request.getVoitureRequest().getId_etatvoiture())

            .categorie(categorie)
            .marque(marque)
            .modele(modele)
            .energie(energie)
            .boite(boite)
            .etatVoiture(etat)

            .kilometrage(request.getVoitureRequest().getKilometrage())
            .matricule(request.getVoitureRequest().getMatricule())
            .build();


        voitureService.save(voiture);
        

        var annonce = service.findById_voiture(voiture.getId());
        if(annonce.size() != 0){
            Format format = Format.builder()
                .code(10)
                .message("Une annonce avec cette voiture existe déja")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        // Voiture voiture = voitureService.findById(request.getId_voiture());

        // if(voiture == null){
        //     Format format = Format.builder()
        //         .code(20)
        //         .message("Cette voiture n'existe pas")
        //         .result(null)
        //         .time(System.currentTimeMillis())
        //         .build();
        //     return ResponseEntity.ok(format);
        // }

        var user = utilisateurService.findByMail(auth.getName());
        

        Annonce nv_annonce = Annonce.builder()
            .id_utilisateur(user.getId())
            .id_Voiture(voiture.getId())
            .datePub(LocalDate.now())
            .prix(request.getPrix())
            .status(request.getStatus())
            .description(request.getDescription())

            .voiture(voiture)
            .utilisateur(user.masquer())
            .build();

        Annonce apres = service.save(nv_annonce);

        for (MultipartFile file : files) {
            try{
                String downloadUrl = Util.uploadFile(file);

                Photo photo = Photo.builder()
                    .id(counterService.getNextSequence(Photo.SEQUENCE_NAME))
                    .id_annonce(apres.getId())
                    .lien(downloadUrl)
                    .build();

                photoService.save(photo);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        apres.setPhotos_base(photoService);

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(apres)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/back-office")
    public ResponseEntity<Format> getAllEnAttente() {
        List<Annonce> annonces = service.findAllEnAttente();
        for (Annonce annonce : annonces) {
            annonce.setUtilisateur(utilisateurService.findById(annonce.getId_utilisateur()).masquer());
            annonce.setVoiture(voitureService.findById(annonce.getId_Voiture())); 
            annonce.setPhotos_base(photoService);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(annonces)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }


    // @PreAuthorize("hasRole('USER')")
    @GetMapping("/offers")
    public ResponseEntity<Format> getAllEnCours() {

        List<Annonce> annonces = service.findOffers();
        for (Annonce annonce : annonces) {
            annonce.setUtilisateur(utilisateurService.findById(annonce.getId_utilisateur()).masquer());
            annonce.setVoiture(voitureService.findById(annonce.getId_Voiture())); 
            annonce.setPhotos_base(photoService);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(annonces)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

    
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    public ResponseEntity<Format> advancedSearch(@RequestBody AnnonceRequestRech request , Authentication auth) {
        var user = utilisateurService.findByMail(auth.getName());

        List<Voiture> voitures = voitureService.findSimilars(request.getVoiture() , request.getKilometrageDeb(), request.getKilometrageFin(), request.getMot_cle());
        System.out.println("size voitures = "+voitures.size());


        List<Annonce> annonces = new ArrayList<>();

        for (Voiture voiture : voitures) {
            Annonce annonce = service.findById_voiture(voiture.getId()).get(0);
            
            if((request.getPrixDeb() == 0 || request.getPrixFin() == 0) || (annonce.getPrix() >= request.getPrixDeb() && annonce.getPrix() <= request.getPrixFin())){


                if((request.getDateDeb() == null || request.getDateFin() == null) || (annonce.getDatePub().isAfter(request.getDateDeb()) && annonce.getDatePub().isBefore(request.getDateFin()))){


                    if((request.getMot_cle() == null || request.getMot_cle().equals("") == true || annonce.getDescription() == null || annonce.getDescription().equals("") == true) || (annonce.getDescription().toLowerCase().contains(request.getMot_cle().toLowerCase()))){
                        annonces.add(annonce);
                    }                        
                }


            }
                    
            

        
        }



        for (Annonce annonce : annonces) {
            annonce.setUtilisateur(utilisateurService.findById(annonce.getId_utilisateur()).masquer());
            annonce.setVoiture(voitureService.findById(annonce.getId_Voiture())); 
            annonce.setPhotos_base(photoService);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(annonces)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

}
