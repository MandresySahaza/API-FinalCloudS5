package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pkg.occasion.api.model.Modele;

public interface ModeleRepository extends JpaRepository<Modele , Integer>{
    List<Modele> findByNomAndAnneeSortie(String nom , int anneeSortie);
}
