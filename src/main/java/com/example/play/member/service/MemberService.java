package com.example.play.member.service;

import com.example.play.member.dto.RequestCreateMemberDto;
import com.example.play.member.dto.RequestUpdateMemberDto;
import com.example.play.member.dto.ResponseDeleteMemberDto;
import com.example.play.member.dto.ResponseMemberDto;
import com.example.play.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    // 멤버 생성
    void createMember(RequestCreateMemberDto memberDto, MultipartFile profile);

    // 멤버 정보 불러오기
    ResponseMemberDto getMember(String email, Long memberId);

    // 멤버 업데이트
    ResponseMemberDto updateMember(String email, RequestUpdateMemberDto updateDto, MultipartFile profile, Long deleteFile);

    // 멤버 삭제
    ResponseDeleteMemberDto deleteMember(String email, Long memberId);

    // 멤버 찾기
    Member findByEmail(String email);

    // 입력한 비밀번호와 멤버의 비밀번호가 일치하는지
    void checkPassword(String password, Member member);
}
