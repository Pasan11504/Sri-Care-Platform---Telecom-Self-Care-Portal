package com.sricare.paymentgatewaymock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessResponse {
    private String status;
    private String message;
    private String transactionId;
}
