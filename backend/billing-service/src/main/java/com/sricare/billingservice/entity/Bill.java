package com.sricare.billingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal billAmount;

    @Column(nullable = false)
    private LocalDate billDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'UNPAID'")
    private String status = "UNPAID";

    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();
}
