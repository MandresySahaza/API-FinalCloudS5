package com.pkg.occasion.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pkg.occasion.api.model.Commission;

import java.math.BigDecimal;
import java.util.List;


public interface CommissionRepository extends JpaRepository<Commission , Integer>{
    @Query("SELECT c FROM Commission c WHERE c.id_Annonce = :id_Annonce")
    List<Commission> findById_annonce(@Param("id_Annonce") int id_Annonce);

    @Query(value = "SELECT SUM((c.valeur / 100) * a.prix) FROM Commissions c JOIN Annonces a ON c.id_Annonce = a.id_Annonce", nativeQuery=true)
    BigDecimal totalBenefice();

    @Query(value = "SELECT SUM(a.prix) FROM Annonces a WHERE a.status = 20", nativeQuery=true)
    BigDecimal totalChiffreAffaire();
}
