package com.sricare.servicemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateServiceRequest {
    private Long userId;
    private String serviceName;
    private String serviceType; // VOICE, DATA, VAS
}
