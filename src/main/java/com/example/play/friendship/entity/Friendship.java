package com.example.play.friendship.entity;

import com.example.play.friendship.constant.FriendshipStatus;
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
public class Friendship extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendShip_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private boolean isFrom;
    private String memberEmail;
    private String friendNickname;
    private String friendEmail;
    private FriendshipStatus status;
    @Column(name = "counterpart_id")
    private Long counterpartId;

    public void acceptFriendshipRequest(){
        this.status = status.ACCEPTED;
    }
    public void setCounterpartId(Long id){
        counterpartId = id;
    }

}
