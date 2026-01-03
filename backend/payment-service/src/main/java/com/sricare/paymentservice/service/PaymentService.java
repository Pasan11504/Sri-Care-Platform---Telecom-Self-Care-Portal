package com.sricare.paymentservice.service;

import com.sricare.paymentservice.dto.ProcessPaymentRequest;
import com.sricare.paymentservice.dto.PaymentResponse;
import com.sricare.paymentservice.entity.Payment;
import com.sricare.paymentservice.repository.PaymentRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${payment.gateway.url}")
    private String paymentGatewayUrl;

    public PaymentResponse processPayment(ProcessPaymentRequest request) {
        // Call payment gateway
        String transactionId = UUID.randomUUID().toString();

        try {
            webClientBuilder.build()
                    .post()
                    .uri(paymentGatewayUrl + "/api/process")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Payment gateway call failed: " + e.getMessage());
        }

        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setBillId(request.getBillId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionId(transactionId);
        payment.setStatus("SUCCESS");

        Payment saved = paymentRepository.save(payment);

        // Publish payment event
        String message = "Payment processed: " + transactionId + " for user " + request.getUserId() + " - Amount: " + request.getAmount();
        rabbitTemplate.convertAndSend("notifications", message);

        return mapToResponse(saved);
    }

    public List<PaymentResponse> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentDetails(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getUserId(), payment.getBillId(),
                payment.getAmount(), payment.getPaymentMethod(), payment.getTransactionId(), payment.getStatus());
    }
}
