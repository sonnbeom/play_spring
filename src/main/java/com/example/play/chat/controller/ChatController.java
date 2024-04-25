package com.example.play.chat.controller;

import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatMessageService chatMessageService;
    @GetMapping()
    public ResponseEntity<List<ChatMessageResponseDto>> getChats(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "chatRoomId") Long chatRoomId){
        List<ChatMessageResponseDto> chats = chatMessageService.getChats(page, chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).body(chats);
    }

}
