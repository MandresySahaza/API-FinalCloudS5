package com.pkg.occasion.api.service;

import java.time.LocalDate;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Utilisateur;
import com.pkg.occasion.api.repository.UtilisateurRepository;
import com.pkg.occasion.api.request.AuthenticationRequest;
import com.pkg.occasion.api.request.RegisterRequest;
import com.pkg.occasion.api.response.AuthenticationResponse;
import com.pkg.occasion.api.response.Format;
import com.pkg.occasion.user.Role;

import lombok.AllArgsConstructor;

@Service
// @Configuration
@AllArgsConstructor
public class AuthenticationService {

    private final UtilisateurRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public Format register(RegisterRequest request){
        var test = repository.findByMail(request.getMail());
        if(test.isPresent()){
            return Format.builder() 
                .code(10)
                .message("L'adresse email est déjà utilisé")
                .result(null)
                .time(System.currentTimeMillis())
                .build()
            ;
        }
        
        var user = Utilisateur.builder()
            .nom(request.getNom())
            .prenom(request.getPrenom())
            .datenaissance(request.getDateNaissance())
            .password(passwordEncoder.encode(request.getPassword()))
            .mail(request.getMail())
            .contact(request.getContact())
            .adresse(request.getAdresse())
            .cin(request.getCin())
            .role(Role.USER)
            .build();
            
        user.setDateinscription(LocalDate.now());
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        
        AuthenticationResponse result = AuthenticationResponse.builder() 
        .token(jwtToken)
        .utilisateur(user)
        .build();
        
        return Format.builder() 
            .code(0)
            .message("OK")
            .result(result)
            .time(System.currentTimeMillis())
            .build()
        ;
    }

    public Format authenticate(AuthenticationRequest request){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword()));
            var user = repository.findByMail(request.getMail())
                .orElseThrow();
            
            if(user.getRole().name().equals("USER") == false){
                throw new Exception("USER only");
            }

            var jwtToken = jwtService.generateToken(user);

            AuthenticationResponse result = AuthenticationResponse.builder() 
            .token(jwtToken)
            .utilisateur(user)
            .build();


            return Format.builder() 
                .code(0)
                .message("OK")
                .result(result)
                .time(System.currentTimeMillis())
                .build()
            ;
        }catch(Exception e){
            e.printStackTrace();
            return Format.builder().build();
        }
        
    }


    public Format authenticate_admin(AuthenticationRequest request){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword()));
            var user = repository.findByMail(request.getMail())
                .orElseThrow();
            
            if(user.getRole().name().equals("ADMIN") == false){
                throw new Exception("ADMIN only");
            }

            var jwtToken = jwtService.generateToken(user);

            AuthenticationResponse result = AuthenticationResponse.builder() 
            .token(jwtToken)
            .utilisateur(user)
            .build();


            return Format.builder() 
                .code(0)
                .message("OK")
                .result(result)
                .time(System.currentTimeMillis())
                .build()
            ;
        }catch(Exception e){
            e.printStackTrace();
            return Format.builder().build();
        }
        
    }


    public Format modify(RegisterRequest request , int id){
        var user = Utilisateur.builder()
            .id(id)
            .nom(request.getNom())
            .prenom(request.getPrenom())
            .datenaissance(request.getDateNaissance())
            .password(passwordEncoder.encode(request.getPassword()))
            .mail(request.getMail())
            .contact(request.getContact())
            .adresse(request.getAdresse())
            .cin(request.getCin())
            .role(Role.USER)
            .build();
            
        Utilisateur original = repository.findById(id).get();
        user.setDateinscription(original.getDateinscription());
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        
        AuthenticationResponse result = AuthenticationResponse.builder() 
        .token(jwtToken)
        .utilisateur(user)
        .build();
        
        return Format.builder() 
            .code(0)
            .message("OK")
            .result(result)
            .time(System.currentTimeMillis())
            .build()
        ;
    }

}
