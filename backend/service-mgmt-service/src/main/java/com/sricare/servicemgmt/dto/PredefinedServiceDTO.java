package com.sricare.servicemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredefinedServiceDTO {
    private Long id;
    private String serviceName;
    private String serviceType;
    private Double price;
    private String description;
    private String billingCycle;
}
