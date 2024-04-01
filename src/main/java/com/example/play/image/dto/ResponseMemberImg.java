package com.example.play.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ResponseMemberImg {
    private Status status;
    private Long id;
    private String url;
    public enum Status{
        DEFAULT, NOT_DEFAULT
    }
}
