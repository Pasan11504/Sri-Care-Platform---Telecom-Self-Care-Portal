package com.sricare.servicemgmt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelecomService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String serviceName;

    @Column(columnDefinition = "VARCHAR(50)")
    private String serviceType; // VOICE, DATA, VAS

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'INACTIVE'")
    private String status = "INACTIVE";

    private LocalDateTime activatedDate;
    private LocalDateTime deactivatedDate;
}
