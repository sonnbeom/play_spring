package com.example.play.chat.service;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chat.domain.ChatRoom;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.dto.ChatRoomWithMessageDto;
import com.example.play.chat.dto.RequestChatRoomDto;
import com.example.play.chat.repository.ChatRoomRepository;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final ChatMessageService chatMessageService;
    public ChatRoomWithMessageDto makeRoom(RequestChatRoomDto chatRoomDto, String email) {
        Member fromMember = memberService.findByEmail(email);
        Member toMember = memberService.findByEmail(chatRoomDto.getOtherEmail());

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByMemberAndOther(fromMember, toMember);

        if (optionalChatRoom.isPresent()){
            ChatRoom chatRoom = optionalChatRoom.get();
            List<ChatMessageResponseDto> chatMessage =  chatMessageService.findByRoom(chatRoom.getId());
            ChatRoomWithMessageDto chatRoomWithMessageDto = new ChatRoomWithMessageDto(chatRoom ,chatMessage);
            return chatRoomWithMessageDto;
        }else {
            ChatRoom chatRoom = createRoom(fromMember, toMember);
            ChatRoomWithMessageDto chatRoomWithMessageDto = new ChatRoomWithMessageDto(chatRoom);
            return chatRoomWithMessageDto;
        }
    }
    private ChatRoom createRoom(Member fromMember, Member toMember){
        ChatRoom chatRoom = new ChatRoom(fromMember, toMember);
        return this.chatRoomRepository.save(chatRoom);
    }

}
