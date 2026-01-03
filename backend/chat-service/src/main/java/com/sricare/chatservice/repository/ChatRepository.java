package com.sricare.chatservice.repository;

import com.sricare.chatservice.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByCustomerId(Long customerId);
}
