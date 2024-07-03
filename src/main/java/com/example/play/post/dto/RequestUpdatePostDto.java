package com.example.play.post.dto;

import com.example.play.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestUpdatePostDto {
    private String title;
    private String content;

    public void sendUpdateTitleToPost(Post post) {
        post.updateTitle(title);
    }

    public void sendUpdateContent(Post post) {
        post.updateContent(content);
    }
}
