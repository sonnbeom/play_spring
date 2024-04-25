package com.example.play.chat.service;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chat.domain.ChatRoom;
import com.example.play.chat.dto.ChatDtoUpdate;
import com.example.play.chat.dto.ChatMessageDto;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.exception.ChatException;
import com.example.play.chat.repository.ChatMessageRepository;
import com.example.play.chat.repository.CustomChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.play.chat.constant.ChatConstant.CHAT_SIZE;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final CustomChatMessageRepository customChatMessageRepository;
    private final ChatRoomService chatRoomService;
    public List<ChatMessageResponseDto> findByRoom(Long roomId) {
        List<ChatMessage> chatMessage = chatMessageRepository.findByRoomNumber(roomId);
        List<ChatMessageResponseDto> dtoList = new ArrayList<>();
        for (ChatMessage c : chatMessage){
            ChatMessageResponseDto chatDto = c.entityToDto();
            dtoList.add(chatDto);
        }
        return dtoList;
    }

    public void save(ChatMessageDto chatMessageDto, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        ChatMessage chatMessage = new ChatMessage(chatMessageDto, chatRoom);
        chatMessageRepository.save(chatMessage);
    }
    public List<ChatMessageResponseDto> getChats(int page, Long chatRoomId) {
        Pageable pageable = PageRequest.of(page , CHAT_SIZE);
        Page<ChatMessage> chatMessages =  customChatMessageRepository.getChats(pageable, chatRoomId);
        List<ChatMessageResponseDto> dtoList = new ArrayList<>();
        if (ObjectUtils.isEmpty(chatMessages)){
            return dtoList;
        }
        for (ChatMessage c : chatMessages){
            ChatMessageResponseDto dto = c.entityToDto();
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<ChatMessageResponseDto> findByRoomIdList(List<Long> chatRoomIdList) {
        List<ChatMessage> chatMessages = customChatMessageRepository.getChatsByRoomIdList(chatRoomIdList);
        List<ChatMessageResponseDto> dtoList = new ArrayList<>();

        for (ChatMessage c: chatMessages){
            ChatMessageResponseDto dto = c.entityToDto();
            dtoList.add(dto);
        }
        return dtoList;
    }

    public Long updateChat(ChatDtoUpdate chatDto, Long chatId) {
        ChatMessage chatMessage = chatMessageRepository.findById(chatId).orElseThrow(()-> new ChatException("해당 ID를 가진 chat을 조회할 수 없습니다: "+ chatId, HttpStatus.NOT_FOUND));
        if (chatDto.getMsg() != null && !chatDto.getMsg().isEmpty()){
            return chatMessage.updateMsg(chatDto);
        }
        return 0L;
    }
}
