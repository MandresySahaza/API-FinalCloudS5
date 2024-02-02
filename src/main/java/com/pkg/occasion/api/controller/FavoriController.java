package com.pkg.occasion.api.controller;

import java.time.LocalDate;
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
import com.pkg.occasion.api.model.Favori;
import com.pkg.occasion.api.request.FavoriRequest;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.AnnonceService;
import com.pkg.occasion.api.service.FavoriService;
import com.pkg.occasion.api.service.PhotoService;
import com.pkg.occasion.api.service.UtilisateurService;
import com.pkg.occasion.api.service.VoitureService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/favoris")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class FavoriController {
    @Autowired
    private FavoriService service;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private AnnonceService annonceService;

    @Autowired
    private VoitureService voitureService;

    @Autowired
    private PhotoService photoService;



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Format> getAll() {
        List<Favori> favoris = service.findAll();

        for(Favori f : favoris){
            f.setUtilisateurAnnonce(utilisateurService, annonceService ,voitureService , photoService);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(favoris)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('ADMIN') or @utilisateurservice.isMyFavori(#id , authentication.principal.username) == true")
    @GetMapping("/{id}")
    public ResponseEntity<Format> getById(@PathVariable int id , Authentication auth) {
        if(!service.existsById(id)){
            Format format = Format.builder() 
                .code(10)
                .message("Ce favori n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Favori favori = service.findById(id);
        favori.setUtilisateurAnnonce(utilisateurService, annonceService , voitureService , photoService);

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(favori)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<Format> create(@RequestBody FavoriRequest request , Authentication auth) {

        var user = utilisateurService.findByMail(auth.getName());

        var favori = service.findById_utilisateurAndId_annonce(user.getId(), request.getId_annonce());

        if(favori.size() != 0){
            Format format = Format.builder()
                .code(10)
                .message("Cet annonce est déja un favori")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Annonce annonce = annonceService.findById(request.getId_annonce());

        if(annonce == null){
            Format format = Format.builder()
                .code(20)
                .message("Cette annonce n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        annonce.setUserVoiture(utilisateurService, voitureService);

        Favori nv_favori = Favori.builder()
            .dateAjout(LocalDate.now())
            .id_Utilisateur(user.getId())
            .id_Annonce(request.getId_annonce())
            .utilisateur(user)
            .annonce(annonce)
            .build();


        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.save(nv_favori))
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
    
    @PreAuthorize("hasRole('ADMIN') or @utilisateurservice.isMyFavori(#id , authentication.principal.username) == true")
    @PutMapping("/{id}")
    public ResponseEntity<Format> update(@PathVariable int id, @RequestBody FavoriRequest request , Authentication auth) {
        
        if(!service.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        var user = utilisateurService.findByMail(auth.getName());
        var other = service.findById_utilisateurAndId_annonce(user.getId(), request.getId_annonce());

        if(other.size() != 0){
            if(other.get(0).getId() != id){
                Format format = Format.builder() 
                    .code(10)
                    .message("Cet annonce est déja un favori")
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
                .message("Cette annonce n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        annonce.setUserVoiture(utilisateurService, voitureService);

        Favori nv_favori = service.findById(id);
        nv_favori.setId_Annonce(request.getId_annonce());
        nv_favori.setUtilisateur(utilisateurService.findById(nv_favori.getId_Utilisateur()));
        nv_favori.setAnnonce(annonceService.findById(request.getId_annonce()));



        // Favori nv_favori = Favori.builder()
        //     .id(id)
        //     .dateAjout(LocalDate.now())
        //     .id_Utilisateur(user.getId())
        //     .id_Annonce(request.getId_annonce())
        //     .utilisateur(user)
        //     .annonce(annonce)
        //     .build();

        service.save(nv_favori);

        Format format = Format.builder() 
            .code(0)
            .message("OK")
            .result(nv_favori)
            .time(System.currentTimeMillis())
            .build();

        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('ADMIN') or @utilisateurservice.isMyFavori(#id , authentication.principal.username) == true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id , Authentication auth) {
        // return entity;
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================================================================================
    // FIN REST methodes
    // ================================================================================

    @PreAuthorize("hasRole('USER')")
    @GetMapping("myfavourites")
    public ResponseEntity<Format> getMyFavourites(Authentication auth) {
        var user = utilisateurService.findByMail(auth.getName());
        
        List<Favori> favoris = service.findMyFavourites(user.getId());

        for(Favori f : favoris){
            f.setUtilisateurAnnonce(utilisateurService, annonceService ,voitureService , photoService);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(favoris)
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }

}
