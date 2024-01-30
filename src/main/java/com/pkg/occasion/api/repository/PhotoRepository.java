package com.pkg.occasion.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pkg.occasion.api.model.Photo;
import java.util.List;


public interface PhotoRepository extends MongoRepository<Photo , Integer>{
    @Query("{'id_annonce' : ?0}")
    public List<Photo> findById_Annonce(int id_annonce);
}
