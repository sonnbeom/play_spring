package com.example.play.member.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.jwt.service.JwtService;
import com.example.play.member.dto.*;
import com.example.play.member.service.MemberServiceImpl;
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
    private final MemberServiceImpl memberServiceImpl;
    private final JwtService jwtService;

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @PostMapping("/join")
    public ResponseEntity createMember(@Valid @RequestPart("memberDto") RequestCreateMemberDto memberDto,
                                                         BindingResult bindingResult,
                                                         @RequestPart(value = "profile", required = false) MultipartFile profile){

        memberServiceImpl.createMember(memberDto, profile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> readMember(@AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseMemberDto dto =  memberServiceImpl.readMember(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @PatchMapping("/{memberId}")
    public ResponseEntity<ResponseMemberDto> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestPart(value = "updateDto", required = false) RequestUpdateMemberDto updateDto,
                                                          @RequestPart(value = "file", required = false)MultipartFile profile,
                                                          @RequestPart(value = "deleteFile", required = false) Long deleteFile){
        ResponseMemberDto updatedDto = memberServiceImpl.updateMember(userDetails.getUsername(), updateDto, profile, deleteFile);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ResponseDeleteMemberDto> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseDeleteMemberDto deleted = memberServiceImpl.deleteMember(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }
}
