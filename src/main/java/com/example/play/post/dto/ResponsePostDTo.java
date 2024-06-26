package com.example.play.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ResponsePostDTo<T> {
    private int currentPage;
    private int totalPages;
    @Builder.Default
    private List<ResponsePostPageDto> postListDto = new ArrayList<>();
}
