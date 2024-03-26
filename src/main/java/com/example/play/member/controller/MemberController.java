package com.example.play.member.controller;

import com.example.play.member.dto.MemberDto;
import com.example.play.member.dto.MemberDtoByReadOne;
import com.example.play.member.dto.MemberUpdateDto;
import com.example.play.member.dto.ResponseUpdatedMemberDto;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    // 비밀번호 찾기, 아이디 찾기
    // 멤버 정보 이름, 사진, 이메일, 친구목록
    // 내가 작성한 글 조회
    // 내가 작성한 댓글 조회
    //채팅 조회
    /*
    * 1. 유저 프로필 조회 : 이름, 사진, 이메일, 친구목록, 작성한 글, 댓글 조회
    * 2. 유저 정보 수정 기능 이메일 비밀번호, 프로필 사진
    *
    *
    * */

    @PostMapping("/create")
    public ResponseEntity<MemberDto> createMember(@Valid @RequestBody MemberDto memberDto,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(memberDto);
        }
        Long l = memberService.createMember(memberDto);
        if (l != 0){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDtoByReadOne> readMember(@PathVariable("memberId") Long memberId){
        MemberDtoByReadOne dto =  memberService.findMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @PatchMapping("/{memberId}")
    public ResponseEntity<ResponseUpdatedMemberDto> updateMember(@PathVariable Long memberId, @RequestBody MemberUpdateDto updateDto){
        ResponseUpdatedMemberDto updatedDto = memberService.updateMember(memberId, updateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }
    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId){
        int deleteSuccess = memberService.deleteMember(memberId);
        if (deleteSuccess == 0){
            return ResponseEntity.status(HttpStatus.OK).build();
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
