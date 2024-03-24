package com.example.play.member.service;

import com.example.play.member.dto.MemberDto;
import com.example.play.member.dto.MemberDtoByReadOne;
import com.example.play.member.dto.MemberUpdateDto;
import com.example.play.member.dto.ResponseUpdatedMemberDto;
import com.example.play.member.entity.Member;
import com.example.play.member.memberMapper.MemberMapper;
import com.example.play.member.repository.MemberRepository;
import com.example.play.exception.MemberNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    public void createMember(MemberDto memberDto) {
        if(duplicateCheck(memberDto)){
            Member member = memberMapper.dtoToMember(memberDto);
            memberRepository.save(member);
        }
    }
    private boolean duplicateCheck(MemberDto memberDto){
        if (memberRepository.existsByEmail(memberDto.getEmail())){
            return false;
        }
        if (memberRepository.existsByNickname(memberDto.getNickname())){
            return false;
        }
        return true;
    }

    public MemberDtoByReadOne findMember(Long memberId) {
        Member findMember = findMemberById(memberId);
        return memberMapper.entityToDto(findMember);
    }

    public ResponseUpdatedMemberDto updateMember(Long memberId, MemberUpdateDto updateDto) {
        Member updateMember = findMemberById(memberId);
        if (updateDto.getNickname() != null && !updateDto.getNickname().isEmpty()){
            updateMember.changeNickname(updateMember.getNickname());
        }
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()){
            updateMember.changePassword(updateDto.getPassword());
        }
        if (updateDto.getPicture() != null && !updateDto.getPicture().isEmpty()){
            updateMember.changePicture(updateDto.getPicture());
        }
        if (updateDto.getEmail() != null && !updateDto.getEmail().isEmpty()){
            updateMember.changeEmail(updateDto.getEmail());
        }
        return updateSuccessCheck(updateMember);
    }

    private ResponseUpdatedMemberDto updateSuccessCheck(Member updateMember) {
        return memberMapper.updateMemberToDto(updateMember);
    }

    public int deleteMember(Long memberId){
       Member member = findMemberById(memberId);
       member.changeStatus();
       return member.getIsActive();
    }
    private Member findMemberById(Long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElseThrow(() -> new MemberNotFoundException(memberId+"로 ID를 가진 유저를 조회할 수 없습니다."));
    }
}
