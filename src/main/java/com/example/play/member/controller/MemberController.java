package com.example.play.member.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.jwt.service.JwtService;
import com.example.play.member.dto.*;
import com.example.play.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final JwtService jwtService;

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
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @PostMapping("/join")
    public ResponseEntity<RequestMemberDto> createMember(@Valid @RequestPart("memberDto") RequestMemberDto memberDto,
                                                         BindingResult bindingResult,
                                                         @RequestPart(value = "profile", required = false) MultipartFile profile){

        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(memberDto);
        }
        Long l = memberService.createMember(memberDto, profile);
        if (l != 0){
            return ResponseEntity.status(HttpStatus.CREATED).body(memberDto);
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
            jwtService.setHeaderAccessToken(response, responseDto.getAccessToken());
//            jwtService.setHeaderRefreshToken(response, responseDto.getRefreshToken());
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", responseDto.getRefreshToken())
                    .httpOnly(true) // 이 옵션을 true로 설정하면, 쿠키가 HTTP(S)를 통해서만 접근 가능하게 됩니다. 즉, 클라이언트 사이드 스크립트(JS 등)에서는 document.cookie를 통해 이 쿠키에 접근할 수 없게 됩니다. 이는 XSS(Cross-site Scripting) 공격으로부터 쿠키를 보호하는 데 도움이 됩니다.
                    .secure(true) //  이 옵션을 true로 설정하면, 쿠키는 HTTPS 프로토콜을 사용하는 요청에서만 전송됩니다. 이는 쿠키가 암호화되지 않은 채널을 통해 전송되는 것을 방지하여, MITM(Man-In-The-Middle) 공격으로부터 보호합니다.
                    .path("/") // 쿠키가 전송될 경로를 지정한다. "/"로 설정하면 도메인의 모든 경로에 대해 쿠키가 전송된다.
                    .maxAge(604800) //7일
                    .domain("localhost") //이 옵션은 쿠키가 전송될 도메인을 지정합니다.
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(responseDto);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> readMember(@AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseMemberDto dto =  memberService.readMember(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @PatchMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestPart(value = "updateDto", required = false) RequestMemberUpdateDto updateDto,
                                                          @RequestPart(value = "file", required = false)MultipartFile profile,
                                                          @RequestPart(value = "deleteFile", required = false) Long deleteFile){
        ResponseMemberDto updatedDto = memberService.updateMember(userDetails.getUsername(), updateDto, profile, deleteFile);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ResponseDeleteMemberDto> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseDeleteMemberDto deleted = memberService.deleteMember(userDetails.getUsername());
        if (deleted.getMemberStatus().equals(ResponseDeleteMemberDto.STATUS.DELETED)
                && deleted.getMemberImgStatus().equals(ResponseDeleteMemberDto.STATUS.DELETED)){
            return ResponseEntity.status(HttpStatus.OK).body(deleted);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(deleted);
        }
    }
}
/*
*  @Operation(summary = "로그인", description = "로그인 후 토큰 발급", tags = { "로그인" })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest login) {
        Users user = Users.builder()
            .email(login.email())
            .password(login.password())
            .build();
        AuthVo authVo = authService.authenticate(user);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authVo.refreshToken())
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(604800)
            .domain("localhost")
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body(new AuthResponse(authVo.accessToken()));
    }*/
