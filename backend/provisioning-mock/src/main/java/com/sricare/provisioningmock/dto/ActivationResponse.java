package com.sricare.provisioningmock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivationResponse {
    private String status;
    private String message;
    private String serviceId;
}
