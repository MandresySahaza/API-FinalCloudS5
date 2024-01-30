package com.pkg.occasion.api.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Document(collection = "messageries")
public class Messagerie {
    @Transient
    public static final String SEQUENCE_NAME = "messagerie_sequence";

    @Id
    @Column(name="id_message")
    public int id;

    public int id_sender;
    public int id_receiver;
    public String message;
    public LocalDateTime time;
}
