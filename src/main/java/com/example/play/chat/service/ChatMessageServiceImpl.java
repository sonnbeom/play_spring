package com.example.play.chat.service;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chat.domain.ChatRoom;
import com.example.play.chat.dto.ChatDtoUpdate;
import com.example.play.chat.dto.ChatMessageDto;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.exception.ChatException;
import com.example.play.chat.exception.ChatRoomException;
import com.example.play.chat.repository.ChatMessageRepository;
import com.example.play.chat.repository.ChatRoomRepository;
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

import static com.example.play.chat.constant.ChatConstant.CHAT_SIZE;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{
    private final ChatMessageRepository chatMessageRepository;
    private final CustomChatMessageRepository customChatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public List<ChatMessageResponseDto> findByRoom(ChatRoom chatRoom) {
        List<ChatMessage> chatMessage = customChatMessageRepository.findByRoomNumber(chatRoom);
        List<ChatMessageResponseDto> dtoList = new ArrayList<>();
        for (ChatMessage c : chatMessage){
            ChatMessageResponseDto chatDto = c.entityToDto();
            dtoList.add(chatDto);
        }
        return dtoList;
    }

    @Override
    public void save(ChatMessageDto chatMessageDto, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomException("해당 id로 chatRoom을 조회할 수 없습니다. " + chatRoomId, HttpStatus.NOT_FOUND));
        ChatMessage chatMessage = new ChatMessage(chatMessageDto, chatRoom);
        chatMessageRepository.save(chatMessage);
    }

    @Override
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

    @Override
    public List<ChatMessage> findByRoomChatRoomList(List<ChatRoom> chatRoomList) {
        return customChatMessageRepository.getChatsByRoomList(chatRoomList);
    }

//    @Override
//    public List<ChatMessageResponseDto> findByRoomChatRoomList(List<ChatRoom> chatRoomList) {
//        List<ChatMessage> chatMessages = customChatMessageRepository.getChatsByRoomList(chatRoomList);
//        List<ChatMessageResponseDto> dtoList = new ArrayList<>();
//
//        for (ChatMessage c: chatMessages){
//            ChatMessageResponseDto dto = c.entityToDto();
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }


    @Override
    public Long updateChat(ChatDtoUpdate chatDto, Long chatId) {
        ChatMessage chatMessage = chatMessageRepository.findById(chatId).orElseThrow(()-> new ChatException("해당 ID를 가진 chat을 조회할 수 없습니다: "+ chatId, HttpStatus.NOT_FOUND));
        if (chatDto.getMsg() != null && !chatDto.getMsg().isEmpty()){
            return chatMessage.updateMsg(chatDto);
        }
        return 0L;
    }
}
