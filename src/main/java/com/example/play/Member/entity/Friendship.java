package com.example.play.Member.entity;

import com.example.play.Member.constant.FriendshipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friendship {
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
