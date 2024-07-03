package com.example.play.member.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.member.dto.*;
import com.example.play.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @PostMapping()
    @Operation(summary = "멤버 회원가입", description = "이메일, 이름, 패스워드, 닉네임, 프로필 사진 등을 받아 회원가입하는 API입니다.")
    @ApiResponse(responseCode = "201", description = "회원가입에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "이메일, 비밀번호가 중복되었습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "사진 등록에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "등록하려는 프로필 사진이 규격에 적합하지 않습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity createMember(@Valid @RequestPart("memberDto") RequestCreateMemberDto memberDto,
                                     @RequestPart(value = "profile", required = false) MultipartFile profile){
        memberService.createMember(memberDto, profile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "멤버 조회", description = "멤버의 id를 통해 상세 내용을 볼 수 있는 API입니다.")
    @ApiResponse(responseCode = "200", description = "멤버 조회에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "로그인한 멤버와 조회하려는 멤버가 일치하지 않습니다.", content = @Content(mediaType = "application/json"))
    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> getMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @PathVariable Long memberId){
        ResponseMemberDto dto =  memberService.getMember(userDetails.getUsername(), memberId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @ApiResponse(responseCode = "200", description = "멤버 정보 수정에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "멤버에게 업데이트 권한이 없습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "사진 등록에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "등록하려는 프로필 사진이 규격에 적합하지 않습니다.", content = @Content(mediaType = "application/json"))
    @Operation(summary = "멤버 정보 수정", description = "이메일, 패스워드, 닉네임, 프로필 사진 등을 업데이트하는 API입니다.")
    @PatchMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestPart(value = "updateDto", required = false) RequestUpdateMemberDto updateDto,
                                                          @RequestPart(value = "file", required = false)MultipartFile profile,
                                                          @RequestPart(value = "deleteFile", required = false) Long deleteFileId){
        ResponseMemberDto updatedDto = memberService.updateMember(userDetails.getUsername(), updateDto, profile, deleteFileId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }
    @Operation(summary = "멤버 삭제", description = "id를 기반으로 회원삭제하는 API입니다.")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ResponseDeleteMemberDto> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable Long memberId){
        ResponseDeleteMemberDto deleted = memberService.deleteMember(userDetails.getUsername(), memberId);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }
}
