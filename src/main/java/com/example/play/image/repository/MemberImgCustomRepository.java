package com.example.play.image.repository;

import com.example.play.image.entity.MemberImage;
import com.example.play.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberImgCustomRepository {
    List<MemberImage> findByMember(Member member);
    List<MemberImage> findByIdAndMember(Long id, Member member);
}
