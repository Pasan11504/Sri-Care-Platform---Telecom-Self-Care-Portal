package com.sricare.servicemgmt.service;

import com.sricare.servicemgmt.dto.ActivateServiceRequest;
import com.sricare.servicemgmt.dto.ServiceResponse;
import com.sricare.servicemgmt.dto.PredefinedServiceDTO;
import com.sricare.servicemgmt.entity.TelecomService;
import com.sricare.servicemgmt.entity.PredefinedService;
import com.sricare.servicemgmt.repository.ServiceRepository;
import com.sricare.servicemgmt.repository.PredefinedServiceRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceManagementService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PredefinedServiceRepository predefinedServiceRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${provisioning.api.url:http://localhost:8087}")
    private String provisioningApiUrl;

    @Value("${billing.api.url:http://localhost:8082}")
    private String billingApiUrl;

    // Get all predefined services
    public List<PredefinedServiceDTO> getPredefinedServices() {
        return predefinedServiceRepository.findAll()
                .stream()
                .map(this::mapToPredefinedDTO)
                .collect(Collectors.toList());
    }

    public ServiceResponse activateService(ActivateServiceRequest request) {
        // Get predefined service details
        PredefinedService predefinedService = predefinedServiceRepository.findByServiceName(request.getServiceName());
        if (predefinedService == null) {
            throw new RuntimeException("Service not found in catalog");
        }

        // Call provisioning API
        try {
            restTemplate.postForObject(
                    provisioningApiUrl + "/api/services/activate",
                    request,
                    String.class
            );
        } catch (Exception e) {
            System.err.println("Provisioning API call failed: " + e.getMessage());
        }

        TelecomService service = new TelecomService();
        service.setUserId(request.getUserId());
        service.setServiceName(request.getServiceName());
        service.setServiceType(request.getServiceType());
        service.setMonthlyCharge(predefinedService.getPrice());
        service.setStatus("ACTIVE");
        service.setActivatedDate(LocalDateTime.now());

        TelecomService saved = serviceRepository.save(service);

        // Create bill for service activation
        createBillForService(request.getUserId(), predefinedService);

        // Publish notification
        String message = "Service " + request.getServiceName() + " activated for user " + request.getUserId();
        rabbitTemplate.convertAndSend("notifications", message);

        return mapToResponse(saved);
    }

    public ServiceResponse reactivateService(Long serviceId) {
        TelecomService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (service.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Service is already active");
        }

        service.setStatus("ACTIVE");
        service.setActivatedDate(LocalDateTime.now());
        service.setDeactivatedDate(null);
        TelecomService updated = serviceRepository.save(service);

        // Create bill for reactivation
        PredefinedService predefinedService = predefinedServiceRepository.findByServiceName(service.getServiceName());
        if (predefinedService != null) {
            createBillForService(service.getUserId(), predefinedService);
        }

        // Publish notification
        String message = "Service " + service.getServiceName() + " reactivated for user " + service.getUserId();
        rabbitTemplate.convertAndSend("notifications", message);

        return mapToResponse(updated);
    }

    public ServiceResponse deactivateService(Long serviceId) {
        TelecomService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setStatus("INACTIVE");
        service.setDeactivatedDate(LocalDateTime.now());
        TelecomService updated = serviceRepository.save(service);

        // Publish notification
        String message = "Service " + service.getServiceName() + " deactivated for user " + service.getUserId();
        rabbitTemplate.convertAndSend("notifications", message);

        return mapToResponse(updated);
    }

    public List<ServiceResponse> getUserServices(Long userId) {
        return serviceRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ServiceResponse> getActiveServices(Long userId) {
        return serviceRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void createBillForService(Long userId, PredefinedService service) {
        try {
            Map<String, Object> billRequest = new HashMap<>();
            billRequest.put("userId", userId);
            billRequest.put("billAmount", service.getPrice());
            billRequest.put("description", "Charge for " + service.getServiceName());
            billRequest.put("billDate", LocalDateTime.now().toLocalDate());
            billRequest.put("dueDate", LocalDateTime.now().toLocalDate().plusDays(30));

            restTemplate.postForObject(
                    billingApiUrl + "/api/bills/create",
                    billRequest,
                    String.class
            );
        } catch (Exception e) {
            System.err.println("Failed to create bill: " + e.getMessage());
        }
    }

    private ServiceResponse mapToResponse(TelecomService service) {
        return new ServiceResponse(service.getId(), service.getUserId(), service.getServiceName(),
                service.getServiceType(), service.getStatus());
    }

    private PredefinedServiceDTO mapToPredefinedDTO(PredefinedService service) {
        return new PredefinedServiceDTO(service.getId(), service.getServiceName(),
                service.getServiceType(), service.getPrice(),
                service.getDescription(), service.getBillingCycle());
    }
}
