package com.example.play.post.entity;

import com.example.play.global.common.entity.BaseEntity;
import com.example.play.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
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
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    public void changeTitle(String title){
        this.title = title;
    }
    public void changeContent(String content){
        this.content = content;
    }
    public void changeIsActive(){
        isActive = 0;
    }
}
