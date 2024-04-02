package com.example.play.member.entity;

import com.example.play.friendship.entity.Friendship;
import com.example.play.image.entity.MemberImage;
import com.example.play.like.post.entity.PostLike;
import com.example.play.member.role.Role;
import com.example.play.global.common.entity.BaseEntity;
import com.example.play.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
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
    @OneToMany(mappedBy = "member")
    private List<Friendship> friendshipList = new ArrayList<>();
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
    public void changePicture(String nickname) {
        this.nickname = nickname;
    }
    public void changeEmail(String email) {
        this.email = email;
    }
    public void changeStatus(){
        this.isActive = 0;
    }
}
