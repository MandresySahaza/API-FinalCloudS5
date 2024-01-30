package com.pkg.occasion.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pkg.occasion.api.model.MonthCount;

public interface MonthCountRepository extends JpaRepository<MonthCount , Integer>{
    
}
