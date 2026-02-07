package com.sricare.chatservice.controller;

import com.sricare.chatservice.dto.ChatMessageRequest;
import com.sricare.chatservice.entity.ChatMessage;
import com.sricare.chatservice.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/chat")
    public ChatMessageRequest handleChatMessage(ChatMessageRequest message) {
        ChatMessage chatMsg = new ChatMessage();
        chatMsg.setCustomerId(message.getCustomerId());
        chatMsg.setAgentId(message.getAgentId());
        chatMsg.setMessage(message.getMessage());
        chatMsg.setSenderType(message.getSenderType());
        chatMsg.setCreatedAt(LocalDateTime.now());
        chatRepository.save(chatMsg);
        return message;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ChatMessage>> getCustomerChat(@PathVariable Long customerId) {
        return ResponseEntity.ok(chatRepository.findByCustomerId(customerId));
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessageRequest request) {
        ChatMessage chatMsg = new ChatMessage();
        chatMsg.setCustomerId(request.getCustomerId());
        chatMsg.setAgentId(request.getAgentId());
        chatMsg.setMessage(request.getMessage());
        chatMsg.setSenderType(request.getSenderType());
        chatMsg.setCreatedAt(LocalDateTime.now());
        ChatMessage saved = chatRepository.save(chatMsg);

        // Generate auto-response from agent
        if ("CUSTOMER".equals(request.getSenderType())) {
            ChatMessage autoResponse = new ChatMessage();
            autoResponse.setCustomerId(request.getCustomerId());
            autoResponse.setAgentId(request.getAgentId() != null ? request.getAgentId() : null); // Agent ID can be null
            autoResponse.setMessage("Thank you for your message. Our support team will respond to you shortly.");
            autoResponse.setSenderType("AGENT");
            autoResponse.setCreatedAt(LocalDateTime.now());
            chatRepository.save(autoResponse);
        }

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chat Service is running!");
    }
}
