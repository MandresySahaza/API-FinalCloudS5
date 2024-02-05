package com.pkg.occasion.api.controller;

import java.time.LocalDateTime;
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

import com.pkg.occasion.api.model.Messagerie;
import com.pkg.occasion.api.request.MessagerieRequest;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.CounterService;
import com.pkg.occasion.api.service.MessagerieService;
import com.pkg.occasion.api.service.UtilisateurService;


@RestController
@RequestMapping("/messageries")
// @RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class MessagerieController {
    @Autowired
    private MessagerieService service;

    @Autowired
    private CounterService counterService;

    @Autowired
    private UtilisateurService utilisateurService;


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

    @PreAuthorize("hasRole('ADMIN') or @messagerieservice.isEnLienAvec(#id , authentication.principal.username)")
    @GetMapping("/{id}")
    public ResponseEntity<Format> getById(@PathVariable int id , Authentication auth) {
        if(!service.existsById(id)){
            Format format = Format.builder() 
                .code(10)
                .message("Ce message n'existe pas")
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

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<Format> create(@RequestBody MessagerieRequest request , Authentication auth) {
        var user = utilisateurService.findByMail(auth.getName());
        // var boite = service.findByNom(request.getNom());

        // if(boite.size() != 0){
        //     Format format = Format.builder()
        //         .code(10)
        //         .message("Une categorie avec ce nom existe déja")
        //         .result(null)
        //         .time(System.currentTimeMillis())
        //         .build();
        //     return ResponseEntity.ok(format);
        // }

        if(user.getId() == request.getId_receiver()){
            Format format = Format.builder()
                .code(10)
                .message("Vous ne pouvez pas vous parlez à vous-même")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Messagerie m = Messagerie.builder() 
            .id(counterService.getNextSequence(Messagerie.SEQUENCE_NAME))
            .id_sender(user.getId())
            .id_receiver(request.getId_receiver())
            .message(request.getMessage())
            .time(LocalDateTime.now())
            .build();

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(service.save(m))
            .time(System.currentTimeMillis())
            .build();
        return ResponseEntity.ok(format);
    }
    
    @PreAuthorize("hasRole('ADMIN') or @messagerieservice.isEnLienAvec(#id , authentication.principal.username)")
    @PutMapping("/{id}")
    public ResponseEntity<Format> update(@PathVariable int id, @RequestBody MessagerieRequest request , Authentication auth) {
        
        if(!service.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        var user = utilisateurService.findByMail(auth.getName());

        // var other = service.findByNom(request.getNom());

        // if(other.size() != 0){
        //     if(other.get(0).getId() != id){
        //         Format format = Format.builder() 
        //             .code(10)
        //             .message("Une categorie avec ce nom existe déjà")
        //             .result(null)
        //             .time(System.currentTimeMillis())
        //             .build();
        //         return ResponseEntity.ok(format);
        //     }
        // }

        Messagerie m = service.findById(id);


        if(user.getId() == m.getId_sender()){
            if(m.getId_sender() == request.getId_receiver()){
                Format format = Format.builder() 
                    .code(10)
                    .message("Vous ne pouvez pas vous parlez à vous même")
                    .result(null)
                    .time(System.currentTimeMillis())
                    .build();
                return ResponseEntity.ok(format);
            }
            m.setId_receiver(request.getId_receiver());
        }else{
            Format format = Format.builder() 
                .code(10)
                .message("Vous n'êtes pas l' envoyeur du message")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }
        
        m.setMessage(request.getMessage());


        Format format = Format.builder() 
            .code(0)
            .message("OK")
            .result(service.save(m))
            .time(System.currentTimeMillis())
            .build();

        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('ADMIN') or @messagerieservice.isEnLienAvec(#id , authentication.principal.username)")
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
    @GetMapping("/mp/{id}")
    public ResponseEntity<Format> getMp(@PathVariable int id , Authentication auth){
        if(utilisateurService.existsById(id) == false){
            return ResponseEntity.notFound().build();
        }

        var moi = utilisateurService.findByMail(auth.getName());

        List<Messagerie> messages = service.messagesEntre2Personnes(id, moi.getId());
        
        Format valiny = Format.builder()
            .code(0)
            .message("OK")
            .result(messages)
            .time(System.currentTimeMillis())
            .build();

        return ResponseEntity.ok(valiny);
    }

}
