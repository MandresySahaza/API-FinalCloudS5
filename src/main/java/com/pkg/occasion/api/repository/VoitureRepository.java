package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pkg.occasion.api.model.Voiture;

public interface VoitureRepository extends MongoRepository<Voiture , Integer>{
    List<Voiture> findByMatricule(String matricule);
}
