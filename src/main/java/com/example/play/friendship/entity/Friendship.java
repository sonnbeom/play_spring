package com.example.play.friendship.entity;

import com.example.play.global.BaseEntity;
import com.example.play.member.constant.FriendshipStatus;
import com.example.play.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friendship extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendShip_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Column
    private FriendshipStatus status;
    @Column
    private boolean isFrom;
    public void acceptFriendshipRequest(){
        this.status = status.ACCEPTED;
    }
}
