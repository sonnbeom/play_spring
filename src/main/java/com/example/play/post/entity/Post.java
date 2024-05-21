package com.example.play.post.entity;

import com.example.play.global.common.entity.BaseEntity;
import com.example.play.image.dto.ResponseImg;
import com.example.play.image.entity.PostImage;
import com.example.play.like.post.entity.PostLike;
import com.example.play.member.entity.Member;
import com.example.play.post.dto.ResponseDeletePostDTo;
import com.example.play.post.dto.ResponsePostOne;
import com.example.play.post.dto.ResponsePostPageDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.play.post.dto.ResponseDeletePostDTo.DeleteStatus.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private int hit;
    @Column(name = "is_active")
    private int isActive;
    @Column(name = "like_count")
    private int likeCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "post")
    private List<PostImage> imageList = new ArrayList<>();
    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes = new ArrayList<>();
    public void upHit(){
        hit += 1;
    }
    public void upLikeCount(){likeCount += 1;}
    public void updateTitle(String title){
        this.title = title;
    }
    public void updateContent(String content){
        this.content = content;
    }
    public int changeIsActive(){
        isActive = 0;
        return isActive;
    }
    public void downLikeCount() {
        likeCount -= 1;
    }
    public boolean checkAuthorization(Member member){
        if (this.member.equals(member)){
            return true;
        }
        return false;
    }
    public ResponsePostOne toDto(){
        return ResponsePostOne.builder()
                .id(id)
                .content(content)
                .title(title)
                .hit(hit)
                .likeCount(likeCount)
                .createdAt(getCreatedAt())
                .build();
    }
    public ResponsePostOne toDtoWithImage(List<ResponseImg> responseImgList) {
        return ResponsePostOne.builder()
                .id(id)
                .content(content)
                .title(title)
                .hit(hit)
                .responseImgList(responseImgList)
                .likeCount(likeCount)
                .createdAt(getCreatedAt())
                .build();
    }
    public ResponsePostPageDto toDtoForPage(){
        return ResponsePostPageDto.builder()
                .id(id)
                .title(title)
                .likeCount(likeCount)
                .url(getThumbnail())
                .hit(hit)
                .build();
    }
    private String getThumbnail(){
        if (!ObjectUtils.isEmpty(imageList)){
            for (PostImage img : imageList){
                if (img.getActiveImgUrl().isPresent()){
                    return img.getActiveImgUrl().get();
                }
            }
        }
        return null;
    }
    public ResponseDeletePostDTo toDeleteDto(){
        return ResponseDeletePostDTo.builder()
                .id(id)
                .status(DELETED)
                .hit(hit)
                .isActive(isActive)
                .likeCount(likeCount)
                .content(content)
                .title(title)
                .build();
    }
}

