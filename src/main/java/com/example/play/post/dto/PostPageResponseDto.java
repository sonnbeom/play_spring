package com.example.play.post.dto;

import com.example.play.image.entity.PostImage;
import com.example.play.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPageResponseDto {
    private Long id;
    private String title;
    private int likeCount;
    private int hit;
    private String url;
    public PostPageResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.likeCount = post.getLikeCount();
        this.hit = post.getHit();
        this.url = getThumbnail(post);
    }
    private String getThumbnail(Post post){
        List<PostImage> imageList = post.getImageList();

        if (imageList != null && !imageList.isEmpty()){
            for (PostImage img : imageList){
                if (img.getIsActive() == 1){
                    return img.getUrl();
                }
            }
        }
        return null;
    }
}
