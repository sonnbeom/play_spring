package com.example.play.friendship.entity;

import com.example.play.friendship.constant.FriendshipStatus;
import com.example.play.friendship.dto.ResponseFriendshipDto;
import com.example.play.global.common.entity.BaseEntity;
import com.example.play.image.dto.ResponseImg;
import com.example.play.image.entity.MemberImage;
import com.example.play.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @JoinColumn(name = "sender_id")
    private Member sender;
    private boolean isReceived;
//    private String memberEmail;
//    private String friendNickname;
//    private String friendEmail;
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;
//    @Column(name = "counterpart_id")
//    private Long counterpartId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    public void acceptFriendshipRequest(){
        this.status = status.ACCEPTED;
    }

    public ResponseFriendshipDto entityToDto() {
        return ResponseFriendshipDto.builder()
                .isReceived(isReceived)
                .status(status)
                .senderDto(sender.entityToDtoWithoutImg())
                .receiverDto(receiver.entityToDtoWithoutImg())
                .build();
    }

    public void createSendMemberList(List<Member> memberListBySend) {
        memberListBySend.add(sender);
    }

    public boolean isMappingWithSenderImg(Map<Member, MemberImage> map) {
        if (map.containsKey(sender)){
            return true;
        }
        return false;
    }

    public Optional<ResponseImg> isExistSenderImg(Map<Member, MemberImage> map) {
        if (map.containsKey(sender)){
            return Optional.of(map.get(sender).entityToDto());
        }
        else return Optional.empty();
    }
}