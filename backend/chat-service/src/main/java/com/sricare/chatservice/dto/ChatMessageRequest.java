package com.sricare.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    private Long customerId;
    private String agentId;
    private String message;
    private String senderType;
}
