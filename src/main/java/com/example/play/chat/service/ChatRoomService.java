package com.example.play.chat.service;

import com.example.play.chat.domain.ChatRoom;
import com.example.play.chat.dto.*;
import com.example.play.chat.exception.ChatRoomException;
import com.example.play.chat.repository.ChatRoomRepository;
import com.example.play.chat.repository.CustomChatRoomRepository;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.play.chat.constant.ChatRoomConstant.CHAT_ROOM_SIZE;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final CustomChatRoomRepository customChatRoomRepository;
    private final MemberService memberService;
    private final ChatMessageService chatMessageService;
    public ChatRoomWithMessageDto makeRoom(RequestChatRoomDto requestChatRoomDto, String email) {
        Member fromMember = memberService.findByEmail(email);
        Member toMember = memberService.findByEmail(requestChatRoomDto.getOtherEmail());

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByMemberAndOther(fromMember, toMember);

        if (optionalChatRoom.isPresent()){
            ChatRoom chatRoom = optionalChatRoom.get();
            List<ChatMessageResponseDto> chatMessage =  chatMessageService.findByRoom(chatRoom.getId());

            ChatRoomDto roomDto = chatRoom.toDto();

            ChatRoomWithMessageDto chatRoomWithMessageDto = ChatRoomWithMessageDto.builder()
                    .chatRoomDto(roomDto)
                    .chatMessage(chatMessage)
                    .build();

            return chatRoomWithMessageDto;
        }else {
            ChatRoom chatRoom = createRoom(fromMember, toMember);
            ChatRoomDto chatRoomDto = chatRoom.toDto();

            ChatRoomWithMessageDto chatRoomWithMessageDto = ChatRoomWithMessageDto.builder()
                    .chatRoomDto(chatRoomDto)
                    .build();

            return chatRoomWithMessageDto;
        }
    }
    private ChatRoom createRoom(Member fromMember, Member toMember){
        ChatRoom chatRoom = ChatRoom.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
        return this.chatRoomRepository.save(chatRoom);
    }
    public ChatRoom findById(Long chatRoomId){
       return chatRoomRepository.findById(chatRoomId).orElseThrow(()-> new ChatRoomException("해당 id를 가진 chatRoom을 조회할 수 없습니다", chatRoomId));
    }
    public List<ChatRoomsWithChatsDto> getChatRooms(int page, String memberEmail) {
        Member member = memberService.findByEmail(memberEmail);
        Pageable pageable = PageRequest.of(page, CHAT_ROOM_SIZE);
        Page<ChatRoom> chatRooms = customChatRoomRepository.findRooms(member, pageable);

        List<Long> chatRoomIdList = new ArrayList<>();
        Map<Long, ChatRoomsWithChatsDto> map = new HashMap<>();

        for (ChatRoom chatRoom: chatRooms){

            chatRoomIdList.add(chatRoom.getId());

            ChatRoomsWithChatsDto chatRoomsWithChatsDto = ChatRoomsWithChatsDto.builder()
                    .chatRoomDto(chatRoom.toDto())
                    .chatMessage(new ChatMessageResponseDto())
                    .build();

            map.put(chatRoom.getId(), chatRoomsWithChatsDto);
        }

        List<ChatMessageResponseDto> chatDtoList = chatMessageService.findByRoomIdList(chatRoomIdList);

        for (ChatMessageResponseDto chatMessageDto: chatDtoList){
            if (map.containsKey(chatMessageDto.getChatRoomId())){
                ChatRoomsWithChatsDto chatRoomsWithChatsDto = map.get(chatMessageDto.getChatRoomId());
                chatRoomsWithChatsDto.insertChatMessage(chatMessageDto);
            }
        }
        return new ArrayList<>(map.values());
    }
}
