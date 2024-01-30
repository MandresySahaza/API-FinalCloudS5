package com.pkg.occasion.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.pkg.occasion.db.Counter;

@Service
public class CounterService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public int getNextSequence(String seqName){
        Query query = new Query(Criteria.where("_id").is(seqName));
        Update update = new Update().inc("seq", 1);
        Counter counter = mongoTemplate.findAndModify(query, update, Counter.class);
        return counter != null ? counter.getSeq() : 1;
    }
}
