package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.MaintOrder;
import com.smartfactory.predictive.backend.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintOrderRepository extends JpaRepository<MaintOrder, Long> {

    @Query("SELECT m FROM MaintOrder m WHERE " +
            "(:lineId IS NULL OR m.deviceId IN " +
            "  (SELECT d.deviceId FROM Device d WHERE d.lineId = :lineId)) AND " +
            "(:status IS NULL OR m.status = :status) " +
            "ORDER BY m.createdAt DESC")
    List<MaintOrder> findByFilters(
            @Param("lineId") String lineId,
            @Param("status") OrderStatus status);

    @Query("SELECT COUNT(m) FROM MaintOrder m WHERE m.deviceId IN " +
            "(SELECT d.deviceId FROM Device d WHERE d.lineId = :lineId) " +
            "AND m.status != 'COMPLETED'")
    long countPendingByLineId(@Param("lineId") String lineId);
}