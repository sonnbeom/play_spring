package com.example.play.member.controller;

import com.example.play.jwt.util.JwtTokenUtil;
import com.example.play.member.dto.*;
import com.example.play.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.play.jwt.constant.HeaderConstant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;

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

    @PostMapping("/join")
    public ResponseEntity<RequestMemberDto> createMember(@Valid @RequestPart("memberDto") RequestMemberDto memberDto,
                                                         BindingResult bindingResult,
                                                         @RequestPart(value = "profile", required = false) MultipartFile profile){

        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(memberDto);
        }
        Long l = memberService.createMember(memberDto, profile);
        if (l != 0){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDto> login(@Valid @RequestBody RequestLogin reqLogin,
                                                  HttpServletResponse response){
        ResponseLoginDto responseDto = memberService.login(reqLogin);
        if (responseDto.isLoginSuccess()){
            String jwtToken = responseDto.getAccessToken();
            String refreshToken = responseDto.getRefreshToken();
            jwtTokenUtil.setHeaderAccessToken(response, jwtToken);
//            jwtTokenUtil.setHeaderRefreshToken(response, refreshToken);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> readMember(@PathVariable("memberId") Long memberId){
        ResponseMemberDto dto =  memberService.readMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @PatchMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> updateMember(@PathVariable Long memberId,
                                                          @RequestPart(value = "updateDto", required = false) RequestMemberUpdateDto updateDto,
                                                          @RequestPart(value = "file", required = false)MultipartFile profile,
                                                          @RequestPart(value = "deleteFile", required = false) Long deleteFile){
        ResponseMemberDto updatedDto = memberService.updateMember(memberId, updateDto, profile, deleteFile);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ResponseDeleteMemberDto> deleteMember(@PathVariable Long memberId){
        ResponseDeleteMemberDto deleted = memberService.deleteMember(memberId);
        if (deleted.getMemberStatus().equals(ResponseDeleteMemberDto.STATUS.DELETED)
                && deleted.getMemberImgStatus().equals(ResponseDeleteMemberDto.STATUS.DELETED)){
            return ResponseEntity.status(HttpStatus.OK).body(deleted);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(deleted);
        }
    }
}
