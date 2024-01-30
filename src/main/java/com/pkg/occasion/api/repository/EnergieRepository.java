package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pkg.occasion.api.model.Energie;

public interface EnergieRepository extends JpaRepository<Energie , Integer>{
    List<Energie> findByNom(String description);
}
