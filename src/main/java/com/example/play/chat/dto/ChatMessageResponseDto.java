package com.example.play.chat.dto;

import com.example.play.chat.domain.ChatMessage;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponseDto {
    private Long id;
    private String msg;
    @Getter
    private Long chatRoomId;
    private LocalDateTime dateTime;
}
