package com.example.play.image.entity;

import com.example.play.global.common.entity.BaseEntity;
import com.example.play.image.dto.ResponseImg;
import com.example.play.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImage extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String url;
    @Column(name = "is_active")
    private int isActive;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    public void changeStatus(){
        isActive = 0;
    }
    public ResponseImg entityToDto(){
        return ResponseImg.builder()
                .id(id)
                .url(url)
                .build();
    }
    public Optional<String> getActiveImgUrl(){
        if (isActive == 1){
            return Optional.of(url);
        }else {
            return Optional.empty();
        }
    }
}
