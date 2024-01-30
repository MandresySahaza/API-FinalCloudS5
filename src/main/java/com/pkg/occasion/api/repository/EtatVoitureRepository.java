package com.pkg.occasion.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pkg.occasion.api.model.EtatVoiture;
import java.util.List;


public interface EtatVoitureRepository extends JpaRepository<EtatVoiture , Integer> {
    List<EtatVoiture> findByNote(double note);
}
