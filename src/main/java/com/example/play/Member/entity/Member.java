package com.example.play.Member.entity;

import com.example.play.global.BaseEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
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
    @Column(unique = true)
    private String phone;
    @Column(name="is_active")
    private Integer isActive;
    @Column
    private String nickname;
    @Column
    private LocalDateTime lastLoginAt;
}
