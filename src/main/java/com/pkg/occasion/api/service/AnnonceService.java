package com.pkg.occasion.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Annonce;
import com.pkg.occasion.api.repository.AnnonceRepository;
import com.pkg.occasion.api.request.AnnonceRequestRech;

@Service
public class AnnonceService {
    @Autowired
    private AnnonceRepository repository;

    public List<Annonce> findAll(){
        return repository.findAll();
    }

    public Annonce findById(int id){
        Optional<Annonce> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<Annonce> findById_voiture(int id_voiture){
        List<Annonce> annonces = repository.findById_voiture(id_voiture);

        return annonces;
    }

    public List<Annonce> findMyAnnonces(int id_utilisateur){
        List<Annonce> annonces = repository.findMyAnnonces(id_utilisateur);

        return annonces;
    }

    public Annonce save(Annonce Annonce){
        return repository.save(Annonce);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }

    public List<Annonce> findAllEnAttente(){
        return repository.findByStatus(0);   
    }

    public List<Annonce> findOffers(){
        return repository.findOffers();
    }
}
