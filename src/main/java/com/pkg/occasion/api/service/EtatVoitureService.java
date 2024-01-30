package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.EtatVoiture;
import com.pkg.occasion.api.repository.EtatVoitureRepository;

@Service
public class EtatVoitureService {
    @Autowired
    private EtatVoitureRepository repository;

    public List<EtatVoiture> findAll(){
        return repository.findAll();
    }

    public EtatVoiture findById(int id){
        Optional<EtatVoiture> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }


    public List<EtatVoiture> findByNote(double note){
        List<EtatVoiture> etats = repository.findByNote(note);

        return etats;
    }

    public EtatVoiture save(EtatVoiture etat){
        return repository.save(etat);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }
    
}
