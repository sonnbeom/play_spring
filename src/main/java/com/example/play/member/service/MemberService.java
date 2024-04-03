package com.example.play.member.service;

import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.service.MemberImgService;
import com.example.play.member.dto.*;
import com.example.play.member.entity.Member;
import com.example.play.member.exception.MemberNotFoundException;
import com.example.play.member.memberMapper.MemberMapper;
import com.example.play.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberImgService memberImgService;
    public Long createMember(RequestMemberDto memberDto, MultipartFile profile) {
        if(duplicateCheck(memberDto)){
            Member member = memberMapper.dtoToMember(memberDto);
            Member saved = memberRepository.save(member);
            if (profile != null && !profile.isEmpty()){
                memberImgService.saveMemberImg(saved, profile);
            }
            return saved.getId();
        }
        else {
            return 0L;
        }
    }
    private boolean duplicateCheck(RequestMemberDto memberDto){
        if (memberRepository.existsByEmail(memberDto.getEmail())){
            return false;
        }
        if (memberRepository.existsByNickname(memberDto.getNickname())){
            return false;
        }
        return true;
    }

    public ResponseMemberDto readMember(Long memberId) {
        Member findMember = findMemberById(memberId);
        ResponseMemberImg img = memberImgService.findByMember(findMember);
        return memberMapper.entityToDto(findMember ,img);
    }

    public ResponseMemberDto updateMember(Long memberId, RequestMemberUpdateDto updateDto, MultipartFile profile, Long deleteFile) {
        Member updateMember = findMemberById(memberId);
        if (updateDto.getNickname() != null && !updateDto.getNickname().isEmpty()){
            updateMember.changeNickname(updateMember.getNickname());
        }
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()){
            updateMember.changePassword(passwordEncoder.encode(updateMember.getPassword()));
        }
        if (updateDto.getPicture() != null && !updateDto.getPicture().isEmpty()){
            updateMember.changePicture(updateDto.getPicture());
        }
        if (updateDto.getEmail() != null && !updateDto.getEmail().isEmpty()){
            updateMember.changeEmail(updateDto.getEmail());
        }
        ResponseMemberImg img = memberImgService.updateStatus(profile, deleteFile, updateMember);
        return memberMapper.entityToDto(updateMember, img);
    }

    public ResponseDeleteMemberDto deleteMember(Long memberId){
       Member member = findMemberById(memberId);
        int imgStatus = memberImgService.changeStatusByMember(member);
        member.changeStatus();
        int memberStatus = member.getIsActive();
        return memberMapper.deleteResponse(memberStatus, imgStatus);
    }
    public Member findMemberById(Long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElseThrow(() -> new MemberNotFoundException(memberId+"로 ID를 가진 유저를 조회할 수 없습니다."));
    }
    public Member findByEmail(String email){
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElseThrow(() -> new MemberNotFoundException(email+ "을 가진 유저를 조회할 수 없습니다."));
    }
}
