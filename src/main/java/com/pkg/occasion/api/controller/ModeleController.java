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

import com.pkg.occasion.api.model.Categorie;
import com.pkg.occasion.api.model.Marque;
import com.pkg.occasion.api.model.Modele;
import com.pkg.occasion.api.request.ModeleRequest;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.CategorieService;
import com.pkg.occasion.api.service.MarqueService;
import com.pkg.occasion.api.service.ModeleService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/modeles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class ModeleController {
    @Autowired
    private ModeleService service;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private MarqueService marqueService;


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

    @GetMapping("/{id}")
    public ResponseEntity<Format> getById(@PathVariable int id) {
        if(!service.existsById(id)){
            Format format = Format.builder() 
                .code(10)
                .message("Ce modele n'existe pas")
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
    public ResponseEntity<Format> create(@RequestBody ModeleRequest request) {

        var modele = service.findByNomAndAnneesortie(request.getNom() , request.getAnneeSortie());

        if(modele.size() != 0){
            Format format = Format.builder()
                .code(10)
                .message("Un modele avec ce nom et cet année de sortie existe déja")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Marque marque = marqueService.findById(request.getId_marque());

        if(marque == null){
            Format format = Format.builder()
                .code(20)
                .message("Cette marque n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Categorie categorie = categorieService.findById(request.getId_categorie());

        if(categorie == null){
            Format format = Format.builder()
                .code(30)
                .message("Cette categorie n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }



        Modele nv_modele = Modele.builder()
            .nom(request.getNom())
            .anneeSortie(request.getAnneeSortie())
            .categorie(categorie)
            .marque(marque)
            .build();


        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.save(nv_modele))
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Format> update(@PathVariable int id, @RequestBody ModeleRequest request) {
        
        if(!service.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        var other = service.findByNomAndAnneesortie(request.getNom() , request.getAnneeSortie());

        if(other.size() != 0){
            if(other.get(0).getId() != id){
                Format format = Format.builder() 
                    .code(10)
                    .message("Un modele avec ce nom et cet année de sortie existe déja")
                    .result(null)
                    .time(System.currentTimeMillis())
                    .build();
                return ResponseEntity.ok(format);
            }
        }

        Marque marque = marqueService.findById(request.getId_marque());

        if(marque == null){
            Format format = Format.builder()
                .code(20)
                .message("Cette marque n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Categorie categorie = categorieService.findById(request.getId_categorie());

        if(categorie == null){
            Format format = Format.builder()
                .code(30)
                .message("Cette categorie n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Modele modele = Modele.builder() 
            .id(id)
            .nom(request.getNom())
            .anneeSortie(request.getAnneeSortie())
            .categorie(categorie)
            .marque(marque)
            .build();

        Format format = Format.builder() 
            .code(0)
            .message("OK")
            .result(service.save(modele))
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



    // ================================================================================
    // FIN REST methodes
    // ================================================================================





    @GetMapping("/marque/{id}")
    public ResponseEntity<Format> getByIdMarque(@PathVariable int id) {
        Marque marque = marqueService.findById(id);

        if(marque == null){
            Format format = Format.builder()
                .code(10)
                .message("Cette marque n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.findByIdmarque(id))
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
}
