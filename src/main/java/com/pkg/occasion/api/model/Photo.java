package com.pkg.occasion.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Document(collection = "photos")
public class Photo {
    @Transient
    public static final String SEQUENCE_NAME = "photos_sequence";

    @Id
    @Column(name="id_photo")
    public int id;
    public int id_annonce;
    public String lien;
}
