package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pkg.occasion.api.model.Marque;

public interface MarqueRepository extends JpaRepository<Marque , Integer>{
    List<Marque> findByNom(String nom);
}
