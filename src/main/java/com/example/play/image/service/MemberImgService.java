package com.example.play.image.service;

import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.domain.MemberImage;
import com.example.play.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberImgService {

    // 멤버와 일치하는 이미지 반환
    ResponseMemberImg findByMember(Member member);

    // 멤버 이미지 저장
    MemberImage saveMemberImg(Member member, MultipartFile profile);

    // 멤버 이미지 업데이트
    ResponseMemberImg updateStatus(MultipartFile profile, Long deleteFileId, Member updateMember);

    // 멤버 이미지 삭제
    int delete(Member member);

    // 멤버 리스트와 상응하는 이미지 리스트 찾기
    List<MemberImage> findImgListByIdList(List<Member> memberList);
}
