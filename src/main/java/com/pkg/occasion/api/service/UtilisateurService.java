package com.pkg.occasion.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Utilisateur;
import com.pkg.occasion.api.repository.AnnonceRepository;
import com.pkg.occasion.api.repository.FavoriRepository;
import com.pkg.occasion.api.repository.UtilisateurRepository;

@Service
@Component("utilisateurservice")
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository; 
    
    @Autowired
    private AnnonceRepository annonceRepository;

    @Autowired
    private FavoriRepository favoriRepository;

    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur findByPwdAndMail(String mail , String password) {
        Optional<Utilisateur> userOptional = utilisateurRepository.findByMailAndPassword(mail, password);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            // Handle the case when no user is found
            return null;
            
            // Alternatively, you can return null or another default value based on your application logic
        }
    }

    public List<Utilisateur> findAll(){
        return utilisateurRepository.findAll();
    }

    public List<Utilisateur> findAll_USER(int id){
        List<Utilisateur> liste = utilisateurRepository.getAllUsers();
        List<Utilisateur> valiny = new ArrayList<Utilisateur>();

        for (Utilisateur utilisateur : liste) {
            if(utilisateur.getId() != id){
                valiny.add(utilisateur);
            }
        }
        return valiny;
    }

    public Utilisateur findById(int id){
        return utilisateurRepository.findById(id).get();
    }

    public Utilisateur findByMail(String mail){
        Optional<Utilisateur> optional = utilisateurRepository.findByMail(mail);
        if(optional.isPresent() == false){
            return null;
        }
        return optional.get();
    }

    public void deleteById(int id){
        utilisateurRepository.deleteById(id);
    }

    public boolean sameUser(Long id, String email) {
        // UtilisateurRepository utilisateurRepository = ApplicationContextProvider.getApplicationContext().getBean(UtilisateurRepository.class);
        
        int id_int = Integer.parseInt(id.toString());

        if(utilisateurRepository.findByMail(email).get().getId() == id_int){
            return true;
        }
        return false;
    }

    public boolean isMyAnnonce(int id_annonce , String email){
        if(annonceRepository.findById(id_annonce).get().getId_utilisateur() == utilisateurRepository.findByMail(email).get().getId()){
            return true;
        }
        return false;
    }

    public boolean isMyFavori(int id_favori , String email){
        if(favoriRepository.findById(id_favori).get().getId_Utilisateur() == utilisateurRepository.findByMail(email).get().getId()){
            return true;
        }
        return false;
    }

    public boolean existsById(int id){
        return utilisateurRepository.existsById(id);
    }
}
