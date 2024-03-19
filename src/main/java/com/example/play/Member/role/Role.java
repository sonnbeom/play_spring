package com.example.play.Member.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    Guest("ROLE-GUEST", "손님"),
    User("ROLE-USER", "일반 사용자");
    private final String key;
    private final String title;
}
