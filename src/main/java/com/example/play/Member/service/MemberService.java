package com.example.play.Member.service;

import com.example.play.Member.dto.MemberDto;
import com.example.play.Member.dto.MemberDtoByReadOne;
import com.example.play.Member.entity.Member;
import com.example.play.Member.memberMapper.MemberMapper;
import com.example.play.Member.repository.MemberRepository;
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
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(() -> new MemberNotFoundException(memberId+"로 ID를 가진 유저를 조회할 수 없습니다."));
        return memberMapper.entityToDto(findMember);
    }

}
