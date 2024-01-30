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

import com.pkg.occasion.api.model.Marque;
import com.pkg.occasion.api.model.PaysMarque;
import com.pkg.occasion.api.request.MarqueRequest;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.MarqueService;
import com.pkg.occasion.api.service.PaysMarqueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/marques")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class MarqueController {
    @Autowired
    private MarqueService service;

    @Autowired
    private PaysMarqueService paysService;


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
                .message("Cette marque n'existe pas")
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
    public ResponseEntity<Format> create(@RequestBody MarqueRequest request) {

        var marque = service.findByNom(request.getNom());

        if(marque.size() != 0){
            Format format = Format.builder()
                .code(10)
                .message("Une marque avec ce nom existe déja")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        PaysMarque pays = paysService.findById(request.getId_paysmarque());

        if(pays == null){
            Format format = Format.builder()
                .code(20)
                .message("Ce pays n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Marque nv_marque = Marque.builder()
            .nom(request.getNom())
            .pays(pays)
            .build();


        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.save(nv_marque))
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Format> update(@PathVariable int id, @RequestBody MarqueRequest request) {
        
        if(!service.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        var other = service.findByNom(request.getNom());

        if(other.size() != 0){
            if(other.get(0).getId() != id){
                Format format = Format.builder() 
                    .code(10)
                    .message("Une marque avec ce nom existe déjà")
                    .result(null)
                    .time(System.currentTimeMillis())
                    .build();
                return ResponseEntity.ok(format);
            }
        }

        PaysMarque pays = paysService.findById(request.getId_paysmarque());

        if(pays == null){
            Format format = Format.builder()
                .code(20)
                .message("Ce pays n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Marque marque = Marque.builder() 
            .id(id)
            .nom(request.getNom())
            .pays(pays)
            .build();

        Format format = Format.builder() 
            .code(0)
            .message("OK")
            .result(service.save(marque))
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
