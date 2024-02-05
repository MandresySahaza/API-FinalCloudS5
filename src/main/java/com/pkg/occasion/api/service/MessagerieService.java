package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Messagerie;
import com.pkg.occasion.api.repository.MessagerieRepository;
import com.pkg.occasion.api.repository.UtilisateurRepository;


@Service
@Component("messagerieservice")
public class MessagerieService {
    @Autowired
    private MessagerieRepository repository; 
    
    @Autowired
    private UtilisateurRepository utilisateurRepository; 
    
    public List<Messagerie> findAll(){
        return repository.findAll();
    }

    public Messagerie findById(int id){
        Optional<Messagerie> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    // public List<Messagerie> findByNom(String nom){
    //     List<Messagerie> Messageries = repository.findByNom(nom);

    //     return Messageries;
    // }

    public Messagerie save(Messagerie Messagerie){
        return repository.save(Messagerie);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }

    public boolean isEnLienAvec(int id , String email){
        int iduser = utilisateurRepository.findByMail(email).get().getId();
        Messagerie m = repository.findById(id).get();
        if(iduser == m.getId_sender() || iduser == m.getId_receiver()){
            return true;
        }
        return false;
    }

    public List<Messagerie> messagesEntre2Personnes(int id1 , int id2){
        return repository.findMessagesBetweenCurrentUserAndFriend(id1, id2);
    }
}
