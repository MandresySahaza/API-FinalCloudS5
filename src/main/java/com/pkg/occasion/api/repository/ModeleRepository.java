package com.pkg.occasion.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pkg.occasion.api.model.Marque;
import com.pkg.occasion.api.model.Modele;

public interface ModeleRepository extends JpaRepository<Modele , Integer>{
    List<Modele> findByNomAndAnneeSortie(String nom , int anneeSortie);

    @Query(value = "select * from modeles m where m.id_marque = :id_marque" , nativeQuery = true)
    List<Modele> findById_marque(@Param("id_marque") int id_marque);
}
