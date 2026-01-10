package com.sricare.servicemgmt.controller;

import com.sricare.servicemgmt.dto.ActivateServiceRequest;
import com.sricare.servicemgmt.dto.ServiceResponse;
import com.sricare.servicemgmt.dto.PredefinedServiceDTO;
import com.sricare.servicemgmt.service.ServiceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin("*")
public class ServiceManagementController {

    @Autowired
    private ServiceManagementService serviceManagementService;

    @GetMapping("/predefined")
    public ResponseEntity<List<PredefinedServiceDTO>> getPredefinedServices() {
        return ResponseEntity.ok(serviceManagementService.getPredefinedServices());
    }

    @PostMapping("/activate")
    public ResponseEntity<ServiceResponse> activateService(@RequestBody ActivateServiceRequest request) {
        return ResponseEntity.ok(serviceManagementService.activateService(request));
    }

    @PutMapping("/{serviceId}/reactivate")
    public ResponseEntity<ServiceResponse> reactivateService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(serviceManagementService.reactivateService(serviceId));
    }

    @PutMapping("/{serviceId}/deactivate")
    public ResponseEntity<ServiceResponse> deactivateService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(serviceManagementService.deactivateService(serviceId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ServiceResponse>> getUserServices(@PathVariable Long userId) {
        return ResponseEntity.ok(serviceManagementService.getUserServices(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<ServiceResponse>> getActiveServices(@PathVariable Long userId) {
        return ResponseEntity.ok(serviceManagementService.getActiveServices(userId));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service Management is running!");
    }
}
