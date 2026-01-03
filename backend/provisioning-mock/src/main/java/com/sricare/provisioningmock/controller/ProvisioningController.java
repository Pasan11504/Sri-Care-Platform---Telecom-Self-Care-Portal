package com.sricare.provisioningmock.controller;

import com.sricare.provisioningmock.dto.ActivationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/services")
@CrossOrigin("*")
public class ProvisioningController {

    @PostMapping("/activate")
    public ResponseEntity<ActivationResponse> activateService(@RequestBody Object request) {
        ActivationResponse response = new ActivationResponse();
        response.setStatus("SUCCESS");
        response.setMessage("Service activated successfully in provisioning system");
        response.setServiceId(UUID.randomUUID().toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deactivate")
    public ResponseEntity<ActivationResponse> deactivateService(@RequestBody Object request) {
        ActivationResponse response = new ActivationResponse();
        response.setStatus("SUCCESS");
        response.setMessage("Service deactivated successfully in provisioning system");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Provisioning Mock is running!");
    }
}
