package com.example.play.chat.controller;

import com.example.play.chat.dto.ChatRoomWithMessageDto;
import com.example.play.chat.dto.RequestChatRoomDto;
import com.example.play.chat.service.ChatRoomService;
import com.example.play.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody RequestChatRoomDto chatRoomDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        ChatRoomWithMessageDto chatRoomWithMessageDto = chatRoomService.makeRoom(chatRoomDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(chatRoomWithMessageDto);
    }
}
