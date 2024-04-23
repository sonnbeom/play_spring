package com.example.play.chat.service;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.repository.ChatMessageRepository;
import com.example.play.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    public List<ChatMessageResponseDto> findByRoom(Long roomId) {
        List<ChatMessage> chatMessage = chatMessageRepository.findByRoomNumber(roomId);
        List<ChatMessageResponseDto> dtoList = new ArrayList<>();
        for (ChatMessage c : chatMessage){
            ChatMessageResponseDto responseDto = new ChatMessageResponseDto(c);
            dtoList.add(responseDto);
        }
        return dtoList;
    }
}
