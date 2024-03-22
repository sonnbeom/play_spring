package com.example.play.Member.entity;

import com.example.play.Member.role.Role;
import com.example.play.global.BaseEntity;
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
    @Column
    private String name;
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @Column(name="is_active")
    private Integer isActive;
    @Column
    private String nickname;
    @Column
    private String picture;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "member")
    private List<Friendship> friendshipList = new ArrayList<>();
    @Builder
    public Member(String name, String email, String picture, Role role, Integer isActive, String nickname, String password){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.isActive = isActive;
        this.nickname = nickname;
        this.password = password;
    }
    @Builder
    public Member(String name, String email, String picture, Role role, Integer isActive){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.isActive = isActive;

    }

    public Member update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }
}
