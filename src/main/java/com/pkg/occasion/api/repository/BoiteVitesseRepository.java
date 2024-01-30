package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pkg.occasion.api.model.BoiteVitesse;

public interface BoiteVitesseRepository extends JpaRepository<BoiteVitesse , Integer>{
    List<BoiteVitesse> findByNom(String description);
}
