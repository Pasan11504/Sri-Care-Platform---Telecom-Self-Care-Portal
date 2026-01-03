package com.sricare.billingservice.repository;

import com.sricare.billingservice.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByUserId(Long userId);
    List<Bill> findByUserIdAndStatus(Long userId, String status);
}
