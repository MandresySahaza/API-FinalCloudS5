package com.pkg.occasion.api.controller;

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

import com.pkg.occasion.api.model.Utilisateur;
import com.pkg.occasion.api.request.AuthenticationRequest;
import com.pkg.occasion.api.request.RegisterRequest;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.api.service.AuthenticationService;
import com.pkg.occasion.api.service.UtilisateurService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/utilisateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private final AuthenticationService authService;


    @PostMapping("")
    public ResponseEntity<Format> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Format> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("admin/login")
    public ResponseEntity<Format> authenticate_admin(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authService.authenticate_admin(request));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Format> getUtilisateurs() {
        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(utilisateurService.findAll())
            .time(System.currentTimeMillis())
        .build();
        return ResponseEntity.ok(format);
    }


    @PreAuthorize("hasRole('ADMIN') or @utilisateurservice.sameUser(#id , authentication.principal.username) == true")
    @GetMapping("/{id}")
    public ResponseEntity<Format> getUtilisateurById(@PathVariable Long id , Authentication auth) {
        int id_int = Integer.parseInt(id.toString());

        if(!utilisateurService.existsById(id_int)){
            Format format = Format.builder() 
                .code(10)
                .message("Cet utilisateur n'existe pas")
                .result(null)
                .time(System.currentTimeMillis())
                .build();
            return ResponseEntity.ok(format);
        }

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(utilisateurService.findById(id_int))
            .time(System.currentTimeMillis())
        .build();
        return ResponseEntity.ok(format);
    }


    // @PreAuthorize("hasRole('ADMIN') or @utilisateurservice.sameUser(#id , authentication.principal.username) == true")
    @GetMapping("/details")
    public ResponseEntity<Format> getUtilisateurByToken(Authentication auth) {
        int id_int = utilisateurService.findByMail(auth.getName()).getId();

        Format format = Format.builder()
            .code(0)
            .message("OK")
            .result(utilisateurService.findById(id_int))
            .time(System.currentTimeMillis())
        .build();
        return ResponseEntity.ok(format);
    }

    @PreAuthorize("hasRole('ADMIN') or @utilisateurservice.sameUser(#id , authentication.principal.username) == true")
    @PutMapping("/{id}")
    public ResponseEntity<Format> updateUtilisateur(@PathVariable Long id, @RequestBody RegisterRequest request) {
        int id_int = Integer.parseInt(id.toString());
        if (!utilisateurService.existsById(id_int)) {
            return ResponseEntity.notFound().build();
        }

        Utilisateur otherUser = utilisateurService.findByMail(request.getMail());
        
        

        if(otherUser != null){

            // Mijery ra efa misy user amnio email io
            if(otherUser.getId() != id_int){
                Format format = Format.builder()
                    .code(10)
                    .message("Un utilisateur avec cette adresse e-mail existe déja")
                    .result(null)
                    .time(System.currentTimeMillis())
                    .build();
                return ResponseEntity.ok(format);
            }
        }

        

        return ResponseEntity.ok(authService.modify(request , id_int));
    }

    @PreAuthorize("hasRole('ADMIN') or @utilisateurservice.sameUser(#id , authentication.principal.username) == true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteById(Integer.parseInt(id.toString()));
        return ResponseEntity.noContent().build();
    }
}
