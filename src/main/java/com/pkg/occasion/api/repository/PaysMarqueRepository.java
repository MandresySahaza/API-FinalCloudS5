package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pkg.occasion.api.model.PaysMarque;

public interface PaysMarqueRepository extends JpaRepository<PaysMarque , Integer>{
    List<PaysMarque> findByNom(String description);
}
