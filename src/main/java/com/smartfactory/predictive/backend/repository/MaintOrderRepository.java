package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.MaintOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintOrderRepository extends JpaRepository<MaintOrder, Long> {
}