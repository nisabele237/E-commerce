package com.example.Test.repository;

import com.example.Test.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findAllByOrderByTimeAsc();
}
