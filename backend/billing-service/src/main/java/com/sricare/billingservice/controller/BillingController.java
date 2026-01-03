package com.sricare.billingservice.controller;

import com.sricare.billingservice.dto.BillResponse;
import com.sricare.billingservice.dto.CreateBillRequest;
import com.sricare.billingservice.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin("*")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping
    public ResponseEntity<BillResponse> createBill(@RequestBody CreateBillRequest request) {
        return ResponseEntity.ok(billingService.createBill(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BillResponse>> getUserBills(@PathVariable Long userId) {
        return ResponseEntity.ok(billingService.getBillsByUser(userId));
    }

    @GetMapping("/user/{userId}/unpaid")
    public ResponseEntity<List<BillResponse>> getUnpaidBills(@PathVariable Long userId) {
        return ResponseEntity.ok(billingService.getUnpaidBills(userId));
    }

    @GetMapping("/{billId}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable Long billId) {
        return ResponseEntity.ok(billingService.getBillById(billId));
    }

    @PutMapping("/{billId}/pay")
    public ResponseEntity<BillResponse> markBillAsPaid(@PathVariable Long billId) {
        return ResponseEntity.ok(billingService.markBillAsPaid(billId));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Billing Service is running!");
    }
}
