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
import com.pkg.occasion.api.request.CategorieRequest;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.CategorieService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class CategorieController {
    @Autowired
    private CategorieService service;


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
                .message("Cette categorie n'existe pas")
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
    public ResponseEntity<Format> create(@RequestBody CategorieRequest request) {

        var boite = service.findByNom(request.getNom());

        if(boite.size() != 0){
            Format format = Format.builder()
                .code(10)
                .message("Une categorie avec ce nom existe déja")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Categorie nv_categorie = Categorie.builder() 
            .nom(request.getNom())
            .build();

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.save(nv_categorie))
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Format> update(@PathVariable int id, @RequestBody CategorieRequest request) {
        
        if(!service.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        var other = service.findByNom(request.getNom());

        if(other.size() != 0){
            if(other.get(0).getId() != id){
                Format format = Format.builder() 
                    .code(10)
                    .message("Une categorie avec ce nom existe déjà")
                    .result(null)
                    .time(System.currentTimeMillis())
                    .build();
                return ResponseEntity.ok(format);
            }
        }

        Categorie categorie = Categorie.builder() 
            .id(id)
            .nom(request.getNom())
            .build();

        Format format = Format.builder() 
            .code(0)
            .message("OK")
            .result(service.save(categorie))
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
