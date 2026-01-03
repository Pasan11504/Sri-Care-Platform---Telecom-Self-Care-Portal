package com.sricare.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequest {
    private Long userId;
    private Long billId;
    private BigDecimal amount;
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
}
