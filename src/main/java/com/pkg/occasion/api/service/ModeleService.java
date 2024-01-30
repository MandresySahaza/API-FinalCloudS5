package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Modele;
import com.pkg.occasion.api.repository.ModeleRepository;

@Service
public class ModeleService {
    @Autowired
    private ModeleRepository repository;

    public List<Modele> findAll(){
        return repository.findAll();
    }

    public Modele findById(int id){
        Optional<Modele> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<Modele> findByNomAndAnneesortie(String nom , int anneeSortie){
        List<Modele> modele = repository.findByNomAndAnneeSortie(nom, anneeSortie);

        return modele;
    }

    public Modele save(Modele Modele){
        return repository.save(Modele);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }
}
