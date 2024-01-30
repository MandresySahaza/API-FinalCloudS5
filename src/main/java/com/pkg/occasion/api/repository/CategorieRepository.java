package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pkg.occasion.api.model.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie , Integer>{
    List<Categorie> findByNom(String nom);
}
