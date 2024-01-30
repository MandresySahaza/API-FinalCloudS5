package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Energie;
import com.pkg.occasion.api.repository.EnergieRepository;

@Service
public class EnergieService {
    @Autowired
    private EnergieRepository repository;

    public List<Energie> findAll(){
        return repository.findAll();
    }

    public Energie findById(int id){
        Optional<Energie> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<Energie> findByNom(String nom){
        List<Energie> energies = repository.findByNom(nom);

        return energies;
    }

    public Energie save(Energie energie){
        return repository.save(energie);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }
}
