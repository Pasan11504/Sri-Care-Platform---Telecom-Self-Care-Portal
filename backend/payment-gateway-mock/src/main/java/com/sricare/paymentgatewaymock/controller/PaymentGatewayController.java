package com.sricare.paymentgatewaymock.controller;

import com.sricare.paymentgatewaymock.dto.PaymentProcessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PaymentGatewayController {

    @PostMapping("/process")
    public ResponseEntity<PaymentProcessResponse> processPayment(@RequestBody Object request) {
        PaymentProcessResponse response = new PaymentProcessResponse();
        response.setStatus("APPROVED");
        response.setMessage("Payment processed successfully");
        response.setTransactionId(UUID.randomUUID().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment Gateway Mock is running!");
    }
}
