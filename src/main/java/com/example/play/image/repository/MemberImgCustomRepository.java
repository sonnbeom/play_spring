package com.example.play.image.repository;

import com.example.play.image.domain.MemberImage;
import com.example.play.member.domain.Member;

import java.util.List;

public interface MemberImgCustomRepository {
    List<MemberImage> findByMember(Member member);
    List<MemberImage> findByIdAndMember(Long id, Member member);

    List<MemberImage> findImgsByMemberList(List<Member> memberList);
}
