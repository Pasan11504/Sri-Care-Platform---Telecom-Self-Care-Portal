package com.sricare.servicemgmt.repository;

import com.sricare.servicemgmt.entity.PredefinedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredefinedServiceRepository extends JpaRepository<PredefinedService, Long> {
    PredefinedService findByServiceName(String serviceName);
}
