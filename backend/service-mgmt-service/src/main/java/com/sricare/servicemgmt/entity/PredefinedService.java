package com.sricare.servicemgmt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "predefined_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredefinedService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String serviceType; // VOICE, DATA, VAS

    @Column(nullable = false)
    private Double price;

    private String description;

    private String billingCycle;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
