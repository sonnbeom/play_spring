package com.example.play.post.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class CreatePostDto {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String title;
    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;
}
