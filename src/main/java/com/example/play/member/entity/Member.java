package com.example.play.member.entity;

import com.example.play.auth.dto.SessionMember;
import com.example.play.friendship.entity.Friendship;
import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.entity.MemberImage;
import com.example.play.like.post.entity.PostLike;
import com.example.play.member.dto.ResponseMemberDto;
import com.example.play.member.exception.MemberDeleteAuthorityException;
import com.example.play.member.exception.MemberGetAuthorityException;
import com.example.play.member.role.Role;
import com.example.play.global.common.entity.BaseEntity;
import com.example.play.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name="is_active")
    private Integer isActive;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "sender")
    private List<Friendship> sentFriendships = new ArrayList<>();
    @OneToMany(mappedBy = "receiver")
    private List<Friendship> receivedFriendships  = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<MemberImage> memberImages = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<PostLike> postLikes =new ArrayList<>();
    @Builder
    public Member(String name, String email, String picture, Role role, Integer isActive, String nickname, String password){
        this.name = name;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.nickname = nickname;
        this.password = password;
    }
    @Builder
    public Member(String name, String email, String picture, Role role, Integer isActive){
        this.name = name;
        this.email = email;
        this.role = role;
        this.isActive = isActive;

    }

    public Member update(String name){
        this.name = name;
        return this;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
    public void changePassword(String password) {
        this.password = password;
    }

    public void changeEmail(String email) {
        this.email = email;
    }
    public int changeStatus(){
        this.isActive = 0;
        return isActive;
    }
    public String getNicknameForChatRoomDto(){
        return nickname;
    }
    public boolean isPassWordMatch(String password, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(password, this.password);
    }
    public ResponseMemberDto toDtoWithOutImg(){
        return ResponseMemberDto.builder()
                .id(id)
                .nickname(nickname)
                .email(email)
                .name(name)
                .build();
    }
    public ResponseMemberDto toDto(ResponseMemberImg img){
        return ResponseMemberDto.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .name(name)
                .img(img)
                .build();
    }
    public Role getRoleForToken(){
        return role;
    }
    public String getEmailForUSerDetail(){
        return email;
    }
    public SessionMember memberToSessionMember(){
        return SessionMember.builder()
                .name(name)
                .email(email)
                .build();
    }

    public void checkDeleteAuthority(Long memberId) {
        if (memberId != id){
            throw new MemberDeleteAuthorityException("해당 아이디가 로그인한 멤버가 일치하지 않습니다. 삭제 시도 아이디: "+ memberId, HttpStatus.FORBIDDEN);
        }
    }

    public void checkGetAuthority(Long memberId) {
        if (memberId != memberId){
            throw new MemberGetAuthorityException("해당 아이디가 로그인한 멤버의 아이디와 일치하지 않습니다. 열람 시도 아이디"+ memberId, HttpStatus.FORBIDDEN);
        }
    }
}
