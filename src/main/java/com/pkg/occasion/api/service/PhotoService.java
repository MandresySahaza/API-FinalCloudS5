package com.pkg.occasion.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Photo;
import com.pkg.occasion.api.repository.PhotoRepository;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository repository;

    public Photo save(Photo photo){
        return repository.save(photo);
    }

    public List<Photo> findById_Annonce(int id_annonce){
        return repository.findById_Annonce(id_annonce);
    }

}
