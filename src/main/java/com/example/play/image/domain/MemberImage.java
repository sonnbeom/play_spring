package com.example.play.image.domain;

import com.example.play.global.common.entity.BaseEntity;
import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Map;

import static com.example.play.image.dto.ResponseMemberImg.Status.*;

@Entity
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
    public int changeStatus(){
        isActive = 0;
        return isActive;
    }

    public void createMapWithMember(Map<Member, MemberImage> map) {
        map.put(member, this);
    }
    public ResponseMemberImg toDto() {
            return new ResponseMemberImg().builder()
                    .status(NOT_DEFAULT)
                    .id(id)
                    .url(url)
                    .build();
    }
}
