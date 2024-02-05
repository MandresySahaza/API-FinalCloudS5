package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pkg.occasion.api.model.Messagerie;

public interface MessagerieRepository extends MongoRepository<Messagerie , Integer>{
    @Query("{ $and: [{ $or: [{ 'id_sender': ?0 }, { 'id_receiver': ?0 }] }, { $or: [{ 'id_sender': ?1 }, { 'id_receiver': ?1 }] }] }")
    List<Messagerie> findMessagesBetweenCurrentUserAndFriend(int currentUserId, int friendId);
}
