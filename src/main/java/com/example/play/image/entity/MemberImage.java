package com.example.play.image.entity;

import com.example.play.global.common.entity.BaseEntity;
import com.example.play.image.dto.ResponseImg;
import com.example.play.member.entity.Member;
import com.example.play.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberImage extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String url;
    @Column(name = "is_active")
    private int isActive;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    public void changeStatus(){
        isActive = 0;
    }

    public void createMapWithMember(Map<Member, MemberImage> map) {
        map.put(member, this);
    }
    public ResponseImg entityToDto(){
        return ResponseImg.builder().
                id(id)
                .url(url)
                .build();
    }
}
