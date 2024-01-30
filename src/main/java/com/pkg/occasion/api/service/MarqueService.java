package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Marque;
import com.pkg.occasion.api.repository.MarqueRepository;

@Service
public class MarqueService {
    @Autowired
    private MarqueRepository repository;

    public List<Marque> findAll(){
        return repository.findAll();
    }

    public Marque findById(int id){
        Optional<Marque> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<Marque> findByNom(String nom){
        List<Marque> marque = repository.findByNom(nom);

        return marque;
    }

    public Marque save(Marque marque){
        return repository.save(marque);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }
}
