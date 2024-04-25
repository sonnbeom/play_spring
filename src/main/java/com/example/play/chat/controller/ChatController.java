package com.example.play.chat.controller;

import com.example.play.chat.dto.ChatDtoUpdate;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.service.ChatMessageService;
import com.example.play.jwt.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    @PatchMapping()
    public ResponseEntity<Long> updateChat(@RequestParam(value = "chatId") Long chatId,
                                        @RequestBody @Valid ChatDtoUpdate chatDto){
        Long updateChatId = chatMessageService.updateChat(chatDto, chatId);
        return ResponseEntity.status(HttpStatus.OK).body(updateChatId);
    }
}
