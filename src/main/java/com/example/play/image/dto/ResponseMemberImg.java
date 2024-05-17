package com.example.play.image.dto;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ResponseMemberImg {
    private Status status;
    private Long id;
    private String url;

    public ResponseMemberImg(Status status) {
        this.status = status;
    }

    public enum Status{
        DEFAULT, NOT_DEFAULT
    }
}
