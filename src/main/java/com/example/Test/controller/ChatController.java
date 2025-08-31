package com.example.Test.controller;

import com.example.Test.model.ChatMessage;
import com.example.Test.repository.ChatRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Tag(name="Chat")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message){
        message.setTime(LocalDateTime.now());
        chatRepository.save(message);
        return message;
    }

    @GetMapping("/appchat")
    public String chat(){
        return "appchat";
    }

    @GetMapping("/historique")
    @Operation(
            summary = "Obtenir tous les messages précédents"
    )
    @ResponseBody
    public List<ChatMessage> getHistorique(){
        return  chatRepository.findAllByOrderByTimeAsc();

    }
}
