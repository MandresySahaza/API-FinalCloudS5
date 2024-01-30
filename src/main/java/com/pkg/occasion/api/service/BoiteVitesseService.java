package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.BoiteVitesse;
import com.pkg.occasion.api.repository.BoiteVitesseRepository;

@Service
public class BoiteVitesseService {
    @Autowired
    private BoiteVitesseRepository repository;

    public List<BoiteVitesse> findAll(){
        return repository.findAll();
    }

    public BoiteVitesse findById(int id){
        Optional<BoiteVitesse> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<BoiteVitesse> findByNom(String description){
        List<BoiteVitesse> boites = repository.findByNom(description);

        return boites;
    }

    public BoiteVitesse save(BoiteVitesse boite){
        return repository.save(boite);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }

}
