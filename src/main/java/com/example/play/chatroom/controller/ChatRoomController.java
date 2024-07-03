package com.example.play.chatroom.controller;

import com.example.play.chatroom.dto.ChatRoomWithMessageDto;
import com.example.play.chatroom.dto.ChatRoomsWithChatsDto;
import com.example.play.chatroom.dto.RequestChatRoomDto;

import com.example.play.chatroom.service.ChatRoomService;
import com.example.play.jwt.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "채팅방 생성", description = "채팅방을 생성하는 API입니다.")
    @PostMapping()
    public ResponseEntity<?> createRoom(@RequestBody RequestChatRoomDto chatRoomDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        ChatRoomWithMessageDto chatRoomWithMessageDto = chatRoomService.makeRoom(chatRoomDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomWithMessageDto);
    }
    @Operation(summary = "채팅방 조회", description = "로그인한 멤버의 채팅방을 조회할 수 있는 API입니다.")
    @GetMapping()
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @RequestParam(value = "page", defaultValue = "0") int page){
        List<ChatRoomsWithChatsDto> chatRooms = chatRoomService.getChatRooms(page, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(chatRooms);
    }

}
