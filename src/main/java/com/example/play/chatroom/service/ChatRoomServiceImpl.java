package com.example.play.chatroom.service;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chat.service.ChatMessageService;
import com.example.play.chatroom.domain.ChatRoom;
import com.example.play.chat.dto.*;
import com.example.play.chatroom.exception.ChatRoomNotFoundException;
import com.example.play.chatroom.repository.ChatRoomRepository;
import com.example.play.chatroom.repository.CustomChatRoomRepository;
import com.example.play.chatroom.dto.ChatRoomDto;
import com.example.play.chatroom.dto.ChatRoomWithMessageDto;
import com.example.play.chatroom.dto.ChatRoomsWithChatsDto;
import com.example.play.chatroom.dto.RequestChatRoomDto;
import com.example.play.member.domain.Member;
import com.example.play.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.play.chatroom.constant.ChatRoomConstant.CHAT_ROOM_SIZE;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;
    private final CustomChatRoomRepository customChatRoomRepository;
    private final MemberService memberService;
    private final ChatMessageService chatMessageService;
    @Override
    public ChatRoomWithMessageDto makeRoom(RequestChatRoomDto requestChatRoomDto, String email) {
        Member member = memberService.findByEmail(email);
        Member other = memberService.findByEmail(requestChatRoomDto.getOtherEmail());

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByMemberAndOther(member, other);

        if (optionalChatRoom.isPresent()){
            ChatRoom chatRoom = optionalChatRoom.get();
            List<ChatMessageResponseDto> chatMessage =  chatMessageService.findByRoom(chatRoom);

            ChatRoomDto roomDto = chatRoom.toDto();

            return ChatRoomWithMessageDto.builder()
                    .chatRoomDto(roomDto)
                    .chatMessage(chatMessage)
                    .build();
        }else {
            ChatRoom chatRoom = createRoom(member, other);
            ChatRoomDto chatRoomDto = chatRoom.toDto();

            return ChatRoomWithMessageDto.builder()
                    .chatRoomDto(chatRoomDto)
                    .build();
        }
    }
    public ChatRoom findById(Long id){
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 id로 chatRoom을 조회할 수 없습니다. " + id, HttpStatus.NOT_FOUND));
    }
    private ChatRoom createRoom(Member member, Member other){
        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .other(other)
                .build();
        return this.chatRoomRepository.save(chatRoom);
    }
    @Override
    public List<ChatRoomsWithChatsDto> getChatRooms(int page, String memberEmail) {
        Member member = memberService.findByEmail(memberEmail);
        Pageable pageable = PageRequest.of(page, CHAT_ROOM_SIZE);
        Page<ChatRoom> chatRooms = customChatRoomRepository.findRooms(member, pageable);

        List<ChatRoom> chatRoomList = new ArrayList<>();
        Map<ChatRoom, ChatRoomsWithChatsDto> map = new HashMap<>();

        for (ChatRoom chatRoom: chatRooms){

            chatRoomList.add(chatRoom);

            ChatRoomsWithChatsDto chatRoomsWithChatsDto = ChatRoomsWithChatsDto.builder()
                    .chatRoomDto(chatRoom.toDto())
                    .chatMessage(new ChatMessageResponseDto())
                    .build();

            map.put(chatRoom, chatRoomsWithChatsDto);
        }
        List<ChatMessage> chatList = chatMessageService.findByRoomChatRoomList(chatRoomList);

        for (ChatMessage chatMessage : chatList){
            Optional<ChatRoomsWithChatsDto> optionalChatRoomsWithChatsDto = chatMessage.findMatchingChatRoom(map);
            optionalChatRoomsWithChatsDto.ifPresent(chatRoomsWithChatsDto -> chatRoomsWithChatsDto.insertChatMessage(chatMessage.toDto()));
        }
        return new ArrayList<>(map.values());
    }
}
