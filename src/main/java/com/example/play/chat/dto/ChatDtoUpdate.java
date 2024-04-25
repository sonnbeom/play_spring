package com.example.play.chat.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ChatDtoUpdate {
    @NotEmpty
    private String msg;
}
