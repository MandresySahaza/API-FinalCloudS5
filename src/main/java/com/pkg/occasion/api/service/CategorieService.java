package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Categorie;
import com.pkg.occasion.api.repository.CategorieRepository;

@Service
public class CategorieService {
@Autowired
    private CategorieRepository repository;

    public List<Categorie> findAll(){
        return repository.findAll();
    }

    public Categorie findById(int id){
        Optional<Categorie> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<Categorie> findByNom(String nom){
        List<Categorie> categories = repository.findByNom(nom);

        return categories;
    }

    public Categorie save(Categorie categorie){
        return repository.save(categorie);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }
}
