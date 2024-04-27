package com.example.play.auth.dto;

import com.example.play.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class SessionMember implements Serializable {
    private String name;
    private String email;
    private String picture;

}
