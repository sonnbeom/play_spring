package com.example.play.chat.controller;

import com.example.play.chat.dto.ChatDtoUpdate;
import com.example.play.chat.dto.ChatMessageResponseDto;

import com.example.play.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatMessageService chatMessageService;

    @Operation(summary = "조회", description = "채팅방에 해당되는 채팅을 가져오는 API입니다.")
    @GetMapping()
    public ResponseEntity<List<ChatMessageResponseDto>> getChats(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "chatRoomId") Long chatRoomId){
        List<ChatMessageResponseDto> chats = chatMessageService.getChats(page, chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).body(chats);
    }
    @Operation(summary = "채팅 수정", description = "채팅을 수정하는 API입니다.")
    @PatchMapping()
    public ResponseEntity<Long> updateChat(@RequestParam(value = "chatId") Long chatId,
                                        @RequestBody @Valid ChatDtoUpdate chatDto){
        Long updateChatId = chatMessageService.updateChat(chatDto, chatId);
        return ResponseEntity.status(HttpStatus.OK).body(updateChatId);
    }
}
