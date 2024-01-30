package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.pkg.occasion.api.model.PaysMarque;
import com.pkg.occasion.api.repository.PaysMarqueRepository;

@Service
public class PaysMarqueService {
    @Autowired
    private PaysMarqueRepository repository;

    public List<PaysMarque> findAll(){
        return repository.findAll();
    }

    public PaysMarque findById(int id){
        Optional<PaysMarque> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<PaysMarque> findByNom(String nom){
        List<PaysMarque> pays = repository.findByNom(nom);

        return pays;
    }

    public PaysMarque save(PaysMarque pays){
        return repository.save(pays);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }
}
