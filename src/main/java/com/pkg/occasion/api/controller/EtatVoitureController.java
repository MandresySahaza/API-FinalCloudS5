package com.pkg.occasion.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pkg.occasion.api.model.EtatVoiture;
import com.pkg.occasion.api.request.EtatVoitureRequest;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.EtatVoitureService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/etatvoitures")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class EtatVoitureController{


    @Autowired
    private EtatVoitureService service;


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
                .message("Cet etat n'existe pas")
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
    public ResponseEntity<Format> create(@RequestBody EtatVoitureRequest request) {

        var etat = service.findByNote(request.getNote());

        if(etat.size() != 0){
            Format format = Format.builder()
                .code(10)
                .message("Un etat avec cette note existe déja")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        EtatVoiture nv_etat = EtatVoiture.builder() 
            .nom(request.getNom())
            .note(request.getNote())
            .build();

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.save(nv_etat))
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Format> update(@PathVariable int id, @RequestBody EtatVoitureRequest request) {
        
        if(!service.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        var other = service.findByNote(request.getNote());

        if(other.size() != 0){
            if(other.get(0).getId() != id){
                Format format = Format.builder() 
                    .code(10)
                    .message("Un etat avec cette note existe déjà")
                    .result(null)
                    .time(System.currentTimeMillis())
                    .build();
                return ResponseEntity.ok(format);
            }
        }

        EtatVoiture etat = EtatVoiture.builder() 
            .id(id)
            .nom(request.getNom())
            .note(request.getNote())
            .build();

        Format format = Format.builder() 
            .code(0)
            .message("OK")
            .result(service.save(etat))
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
