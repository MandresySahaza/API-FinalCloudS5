package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Favori;
import com.pkg.occasion.api.repository.FavoriRepository;

@Service
public class FavoriService {
    @Autowired
    private FavoriRepository repository;

    public List<Favori> findAll(){
        return repository.findAll();
    }

    public List<Favori> findMyFavourites(int id_utilisateur){
        return repository.findMyFavourites(id_utilisateur);
    }

    public Favori findById(int id){
        Optional<Favori> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<Favori> findById_utilisateurAndId_annonce(int id_utilisateur , int id_annonce){
        List<Favori> favori = repository.findById_utilisateurAndId_annonce(id_utilisateur , id_annonce);

        return favori;
    }

    public Favori save(Favori Favori){
        return repository.save(Favori);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }
}
