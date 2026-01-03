package com.sricare.servicemgmt.service;

import com.sricare.servicemgmt.dto.ActivateServiceRequest;
import com.sricare.servicemgmt.dto.ServiceResponse;
import com.sricare.servicemgmt.entity.TelecomService;
import com.sricare.servicemgmt.repository.ServiceRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceManagementService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${provisioning.api.url}")
    private String provisioningApiUrl;

    public ServiceResponse activateService(ActivateServiceRequest request) {
        // Call provisioning API
        try {
            webClientBuilder.build()
                    .post()
                    .uri(provisioningApiUrl + "/api/services/activate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            // Log and continue (graceful degradation)
            System.err.println("Provisioning API call failed: " + e.getMessage());
        }

        TelecomService service = new TelecomService();
        service.setUserId(request.getUserId());
        service.setServiceName(request.getServiceName());
        service.setServiceType(request.getServiceType());
        service.setStatus("ACTIVE");
        service.setActivatedDate(LocalDateTime.now());

        TelecomService saved = serviceRepository.save(service);

        // Publish notification
        String message = "Service " + request.getServiceName() + " activated for user " + request.getUserId();
        rabbitTemplate.convertAndSend("notifications", message);

        return mapToResponse(saved);
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

    private ServiceResponse mapToResponse(TelecomService service) {
        return new ServiceResponse(service.getId(), service.getUserId(), service.getServiceName(),
                service.getServiceType(), service.getStatus());
    }
}
