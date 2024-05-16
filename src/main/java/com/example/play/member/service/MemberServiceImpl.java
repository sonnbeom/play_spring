package com.example.play.member.service;

import com.example.play.auth.exception.PasswordNotMatchException;
import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.service.MemberImgService;
import com.example.play.member.dto.*;
import com.example.play.member.entity.Member;
import com.example.play.member.exception.DuplicateMemberEmailException;
import com.example.play.member.exception.DuplicateMemberNicknameException;
import com.example.play.member.exception.MemberNotFoundException;
import com.example.play.member.memberMapper.MemberMapper;
import com.example.play.member.repository.MemberCustomRepository;
import com.example.play.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberImgService memberImgService;
    private final MemberCustomRepository memberCustomRepository;

    @Override
    public void createMember(RequestCreateMemberDto memberDto, MultipartFile profile) {
        duplicateCheck(memberDto);
        Member member = memberMapper.dtoToMember(memberDto);
        Member saved = memberRepository.save(member);
        if (!ObjectUtils.isEmpty(profile)){
            memberImgService.saveMemberImg(saved, profile);
        }
    }

    private void duplicateCheck(RequestCreateMemberDto memberDto) {
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new DuplicateMemberEmailException("해당 이메일을 가진 유저가 존재합니다. 이메일:"+ memberDto.getEmail() ,HttpStatus.BAD_REQUEST);
        }
        if (memberRepository.existsByNickname(memberDto.getNickname())) {
            throw new DuplicateMemberNicknameException("해당 이메일을 가진 유저가 존재합니다. 닉네임:"+ memberDto.getEmail() ,HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseMemberDto getMember(String email) {
        Member member = findByEmail(email);
        ResponseMemberImg img = memberImgService.findByMember(member);
        return member.toDto(img);
    }

    @Override
    public ResponseMemberDto updateMember(String email, RequestUpdateMemberDto updateDto, MultipartFile profile, Long deleteFile) {
        Member member = findByEmail(email);
        if (updateDto.isUpdateNicknamePresent()){
            updateDto.sendNicknameToMember(member);
        }
        if (updateDto.isUpdatePwdPresent()){
            updateDto.sendUpdatePwdToMember(member);
        }
        if (updateDto.isUpdateEmailPresent()){
            updateDto.sendUpdateEmailToMember(member);
        }
        ResponseMemberImg img = memberImgService.updateStatus(profile, deleteFile, member);
        return member.toDto(img);
    }

    @Override
    public ResponseDeleteMemberDto deleteMember(String email) {
        Member member = findByEmail(email);
        int imgStatus = memberImgService.delete(member);
        int statusResult = member.changeStatus();
        return memberMapper.deleteResponse(statusResult, imgStatus);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email + "을 가진 유저를 조회할 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    @Override
    public void checkPassword(String password, Member member) {
        if (!member.isPassWordMatch(password, passwordEncoder)){
            log.info("비밀번호가 일치하지 않습니다. 입력 비밀번호 실제 비밀번호: {}", password);
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다. ", HttpStatus.BAD_REQUEST);
        }
    }
}
