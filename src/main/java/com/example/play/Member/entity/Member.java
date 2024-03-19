package com.example.play.Member.entity;

import com.example.play.Member.role.Role;
import com.example.play.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(unique = true)
    private String email;
    @Column
    private String password;
//    @Column(unique = true)
//    private String phone;
    @Column(name="is_active")
    private Integer isActive;
    @Column
    private String nickname;
    @Column
    private String picture;
    @Enumerated(EnumType.STRING)
    private Role role;
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
