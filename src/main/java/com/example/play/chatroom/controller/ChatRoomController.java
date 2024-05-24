package com.example.play.chatroom.controller;

import com.example.play.chatroom.dto.ChatRoomWithMessageDto;
import com.example.play.chatroom.dto.ChatRoomsWithChatsDto;
import com.example.play.chatroom.dto.RequestChatRoomDto;

import com.example.play.chatroom.service.ChatRoomService;
import com.example.play.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatRoom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping()
    public ResponseEntity<?> createRoom(@RequestBody RequestChatRoomDto chatRoomDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        ChatRoomWithMessageDto chatRoomWithMessageDto = chatRoomService.makeRoom(chatRoomDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomWithMessageDto);
    }
    @GetMapping()
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @RequestParam(value = "page", defaultValue = "0") int page){
        List<ChatRoomsWithChatsDto> chatRooms = chatRoomService.getChatRooms(page, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(chatRooms);
    }

}
