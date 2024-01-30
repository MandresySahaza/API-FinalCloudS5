package com.pkg.occasion.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Annonce;
import com.pkg.occasion.api.model.Voiture;
import com.pkg.occasion.api.repository.VoitureRepository;
import com.pkg.occasion.api.request.AnnonceRequestRech;
import com.pkg.occasion.api.request.VoitureRequest;

@Service
public class VoitureService {
    @Autowired
    private VoitureRepository repository;

    public List<Voiture> findAll(){
        return repository.findAll();
    }

    public Voiture findById(int id){
        Optional<Voiture> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<Voiture> findByMatricule(String matricule){
        List<Voiture> Voiture = repository.findByMatricule(matricule);
        
        return Voiture;
    }

    public Voiture save(Voiture Voiture){
        return repository.save(Voiture);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    } 

    public boolean existsByMatricule(String matricule){
        return repository.findByMatricule(matricule).size() != 0;
    }

    public List<Voiture> findSimilars(VoitureRequest request , double kilometrageDeb , double kilometrageFin , String motcle){
        List<Voiture> val = new ArrayList<>();

        Voiture voiture = Voiture.builder() 
            .id_categorie(request.getId_categorie())
            .id_marque(request.getId_marque())
            .id_modele(request.getId_modele())
            .id_energie(request.getId_energie())
            .id_boitevitesse(request.getId_boitevitesse())
            .id_etatvoiture(request.getId_etatvoiture())
            .build();

        List<Voiture> tous = repository.findAll();
        System.out.println("voitures avant fitre : "+tous.size());

        for (Voiture voiture2 : tous) {
            if( request.getId_categorie() == 0 || request.getId_categorie() == voiture2.getId_categorie()){

                if(request.getId_marque() == 0 || request.getId_marque() == voiture2.getId_marque()){
                    
                    if(request.getId_modele() == 0 || request.getId_modele() == voiture2.getId_modele()){
                        
                        if(request.getId_energie() == 0 || request.getId_energie() == voiture2.getId_energie()){
                            
                            if(request.getId_boitevitesse() == 0 || request.getId_boitevitesse() == voiture2.getId_boitevitesse()){
                              
                              if(request.getId_etatvoiture() == 0 || request.getId_etatvoiture() == voiture2.getId_etatvoiture()){
                                    
                                    if((kilometrageDeb != 0 && kilometrageFin != 0) || voiture2.getKilometrage() >= kilometrageDeb && voiture2.getKilometrage() <= kilometrageFin){
                                        
                                        if((motcle == null || motcle.equals("") == false || request.getMatricule() == null || request.getMatricule().equals("")) || (motcle.toLowerCase().contains(voiture2.getMatricule().toLowerCase()))){
                                            
                                            val.add(voiture2);        
                                        } 

                                    }
                                    
                                    
                    
                                }
                
                            }
            
                        }
                        
                    }
    
                }

            }
        }
        return val;
    }
}
