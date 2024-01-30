package com.pkg.occasion.db;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;

@Document(collection = "counters")
@Data
public class Counter {
    @Id
    private String id;
    private int seq;
}
