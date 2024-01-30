package com.pkg.occasion.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pkg.occasion.api.model.Messagerie;

public interface MessagerieRepository extends MongoRepository<Messagerie , Integer>{
    
}
