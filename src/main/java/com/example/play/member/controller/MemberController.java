package com.example.play.member.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.member.dto.*;
import com.example.play.member.service.MemberService;
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
    public ResponseEntity createMember(@Valid @RequestPart("memberDto") RequestCreateMemberDto memberDto,
                                     @RequestPart(value = "profile", required = false) MultipartFile profile){
        memberService.createMember(memberDto, profile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> getMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @PathVariable Long memberId){
        ResponseMemberDto dto =  memberService.getMember(userDetails.getUsername(), memberId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @PatchMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestPart(value = "updateDto", required = false) RequestUpdateMemberDto updateDto,
                                                          @RequestPart(value = "file", required = false)MultipartFile profile,
                                                          @RequestPart(value = "deleteFile", required = false) Long deleteFileId){
        ResponseMemberDto updatedDto = memberService.updateMember(userDetails.getUsername(), updateDto, profile, deleteFileId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ResponseDeleteMemberDto> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable Long memberId){
        ResponseDeleteMemberDto deleted = memberService.deleteMember(userDetails.getUsername(), memberId);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }
}
