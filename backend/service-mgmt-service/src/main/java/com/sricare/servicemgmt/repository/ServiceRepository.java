package com.sricare.servicemgmt.repository;

import com.sricare.servicemgmt.entity.TelecomService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<TelecomService, Long> {
    List<TelecomService> findByUserId(Long userId);
    List<TelecomService> findByUserIdAndStatus(Long userId, String status);
}
