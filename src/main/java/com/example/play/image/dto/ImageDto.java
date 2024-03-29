package com.example.play.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class ImageDto {
    private Status status;
    private String path;
    public enum Status{
        UPLOADED, FAIL
    }
}
