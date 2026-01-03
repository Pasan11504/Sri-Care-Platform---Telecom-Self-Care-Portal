package com.sricare.billingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    private Long id;
    private Long userId;
    private BigDecimal billAmount;
    private LocalDate billDate;
    private LocalDate dueDate;
    private String status;
    private String description;
}
