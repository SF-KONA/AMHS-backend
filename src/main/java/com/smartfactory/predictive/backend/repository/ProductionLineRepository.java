package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine, String> {
    List<ProductionLine> findByFabId(String fabId);
}