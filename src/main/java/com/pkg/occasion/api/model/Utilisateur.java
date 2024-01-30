package com.pkg.occasion.api.model;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pkg.occasion.user.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name="Utilisateurs")
public class Utilisateur implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    public Integer id;

    public String nom;
    public String prenom;
    public LocalDate datenaissance;
    public LocalDate dateinscription;

    @JsonIgnore
    public String password;
    public String mail;
    public String contact;
    public String adresse;
    public String cin;

    
    @Enumerated(EnumType.STRING)
    private Role role;


    public void display() throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        System.out.println("{");
        for (Field field : fields) {
            field.setAccessible(true);
            System.out.println(field.getName() + ": " + field.get(this));
        }
        System.out.println("}");
    }

    public UtilisateurMasque masquer(){
        return UtilisateurMasque.builder() 
            .id(id)
            .nom(nom)
            .prenom(prenom)
            .build();
    }
    

    
    
    // public Utilisateur(){}

    // public Utilisateur(String nom , String prenom , String datenaissance , String password , String mail , String contact , String adresse , String cin , int status){
    //     this.nom = nom;
    //     this.prenom = prenom;
    //     this.datenaissance = LocalDate.parse(datenaissance);
    //     this.dateinscription = LocalDate.now();
    //     this.password = password;
    //     this.mail = mail;
    //     this.contact = contact;
    //     this.adresse = adresse;
    //     this.cin = cin;
    //     this.status = status;

    // }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
    
    @Override
    public String getUsername() {
        return getMail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
