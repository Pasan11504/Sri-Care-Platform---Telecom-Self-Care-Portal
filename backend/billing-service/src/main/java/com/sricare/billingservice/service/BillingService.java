package com.sricare.billingservice.service;

import com.sricare.billingservice.dto.BillResponse;
import com.sricare.billingservice.dto.CreateBillRequest;
import com.sricare.billingservice.entity.Bill;
import com.sricare.billingservice.repository.BillRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public BillResponse createBill(CreateBillRequest request) {
        Bill bill = new Bill();
        bill.setUserId(request.getUserId());
        bill.setBillAmount(request.getBillAmount());
        bill.setBillDate(request.getBillDate());
        bill.setDueDate(request.getDueDate());
        bill.setDescription(request.getDescription());
        bill.setStatus("UNPAID");
        bill.setCreatedAt(LocalDateTime.now());

        Bill saved = billRepository.save(bill);

        // Publish notification event
        String message = "New bill generated for user " + request.getUserId() + " - Amount: " + request.getBillAmount();
        rabbitTemplate.convertAndSend("notifications", message);

        return mapToResponse(saved);
    }

    public List<BillResponse> getBillsByUser(Long userId) {
        return billRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BillResponse> getUnpaidBills(Long userId) {
        return billRepository.findByUserIdAndStatus(userId, "UNPAID")
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BillResponse getBillById(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        return mapToResponse(bill);
    }

    public BillResponse markBillAsPaid(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        bill.setStatus("PAID");
        Bill updated = billRepository.save(bill);

        // Publish payment notification
        String message = "Bill " + billId + " marked as paid for user " + bill.getUserId();
        rabbitTemplate.convertAndSend("notifications", message);

        return mapToResponse(updated);
    }

    private BillResponse mapToResponse(Bill bill) {
        return new BillResponse(bill.getId(), bill.getUserId(), bill.getBillAmount(),
                bill.getBillDate(), bill.getDueDate(), bill.getStatus(), bill.getDescription());
    }
}
