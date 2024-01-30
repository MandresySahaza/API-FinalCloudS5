package com.pkg.occasion.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Commission;
import com.pkg.occasion.api.repository.CommissionRepository;

@Service
public class CommissionService {
    @Autowired
    private CommissionRepository repository;

    public List<Commission> findAll(){
        return repository.findAll();
    }

    public Commission findById(int id){
        Optional<Commission> optional = repository.findById(id);

        if(optional.isPresent() == false){
            return null;
        }

        return optional.get();
    }

    public List<Commission> findById_Annonce(int id_annonce){
        List<Commission> commissions = repository.findById_annonce(id_annonce);

        return commissions;
    }

    public Commission save(Commission commission){
        return repository.save(commission);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public boolean existsById(int id){
        return repository.existsById(id);
    }
}
