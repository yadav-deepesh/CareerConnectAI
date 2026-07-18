package com.careerconnect.controller;

import com.careerconnect.dto.request.ChatRequest;
import com.careerconnect.dto.response.ChatResponse;
import com.careerconnect.service.CareerAssistantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final CareerAssistantService careerAssistantService;

    public ChatController(CareerAssistantService careerAssistantService) {
        this.careerAssistantService = careerAssistantService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> askAssistant(@Valid @RequestBody ChatRequest request) {
        String answer = careerAssistantService.handleChat(
                request.getStudentId(),
                request.getDriveId(),
                request.getMessage()
        );

        String modelName = careerAssistantService.getModelName();
        ChatResponse response = new ChatResponse(answer, modelName);

        return ResponseEntity.ok(response);
    }
}
