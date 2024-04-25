package com.example.play.member.service;

import com.example.play.auth.exception.PasswordNotMatchException;
import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.service.MemberImgService;
import com.example.play.jwt.dto.TokenDto;
import com.example.play.jwt.exception.InvalidLoginException;
import com.example.play.jwt.service.JwtService;
import com.example.play.member.dto.*;
import com.example.play.member.entity.Member;
import com.example.play.member.exception.DuplicateMemberEmailException;
import com.example.play.member.exception.DuplicateMemberNicknameException;
import com.example.play.member.exception.MemberNotFoundException;
import com.example.play.member.memberMapper.MemberMapper;
import com.example.play.member.repository.MemberCustomRepository;
import com.example.play.member.repository.MemberRepository;
import com.example.play.redis.service.RedisService;
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
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberImgService memberImgService;
    private final MemberCustomRepository memberCustomRepository;
    private final JwtService jwtService;
    private final RedisService redisService;

    public Long createMember(RequestMemberDto memberDto, MultipartFile profile) {
        duplicateCheck(memberDto);
        Member member = memberMapper.dtoToMember(memberDto);
        Member saved = memberRepository.save(member);
        if (!ObjectUtils.isEmpty(profile)){
            memberImgService.saveMemberImg(saved, profile);
        }
        return saved.getId();
    }

    private void duplicateCheck(RequestMemberDto memberDto) {
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new DuplicateMemberEmailException("해당 이메일을 가진 유저가 존재합니다. 이메일:"+ memberDto.getEmail() ,HttpStatus.BAD_REQUEST);
        }
        if (memberRepository.existsByNickname(memberDto.getNickname())) {
            throw new DuplicateMemberNicknameException("해당 이메일을 가진 유저가 존재합니다. 닉네임:"+ memberDto.getEmail() ,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseMemberDto readMember(String email) {
        Member member = findByEmail(email);
        ResponseMemberImg img = memberImgService.findByMember(member);
        return memberMapper.entityToDto(member, img);
    }

    public ResponseMemberDto updateMember(String email, RequestMemberUpdateDto updateDto, MultipartFile profile, Long deleteFile) {
        Member updateMember = findByEmail(email);
        if (!ObjectUtils.isEmpty(updateDto.getNickname())) {
            updateMember.changeNickname(updateMember.getNickname());
        }
        if (!ObjectUtils.isEmpty(updateDto.getPassword())) {
            updateMember.changePassword(passwordEncoder.encode(updateMember.getPassword()));
        }
        if (!ObjectUtils.isEmpty(updateDto.getEmail())) {
            updateMember.changeEmail(updateDto.getEmail());
        }
        ResponseMemberImg img = memberImgService.updateStatus(profile, deleteFile, updateMember);
        return memberMapper.entityToDto(updateMember, img);
    }

    public ResponseDeleteMemberDto deleteMember(String email) {
        Member member = findByEmail(email);
        int imgStatus = memberImgService.changeStatusByMember(member);
        member.changeStatus();
        int memberStatus = member.getIsActive();
        return memberMapper.deleteResponse(memberStatus, imgStatus);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId + "로 ID를 가진 유저를 조회할 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(email + "을 가진 유저를 조회할 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    public ResponseLoginDto login(RequestLogin reqLogin) {
        Member member = findByEmail(reqLogin.getMemberEmail());
        if (!passwordEncoder.matches(reqLogin.getPassword(), member.getPassword())) {
            return ResponseLoginDto.builder()
                    .password(reqLogin.getPassword())
                    .email(reqLogin.getMemberEmail())
                    .loginSuccess(false)
                    .build();
        } else {
            TokenDto tokenDto = jwtService.provideToken(member.getEmail(), member.getRole());
            redisService.setValues(tokenDto.getRefreshToken(), tokenDto.getRefreshToken());
            return ResponseLoginDto.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .role(member.getRole())
                    .email(member.getEmail())
                    .isActive(member.getIsActive())
                    .loginSuccess(true)
                    .nickname(member.getNickname())
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getRefreshToken())
                    .build();
        }
    }

    public Member getLoginByMemberId(String loginId) {
        try {
            Long id = Long.parseLong(loginId);
            return findMemberById(id);
        } catch (NumberFormatException e) {
            log.info(" memberService: 해당 loginId를 Long으로 형변환할 수 없습니다: {} ", loginId);
            throw new InvalidLoginException("로그인 ID 형식이 올바르지 않습니다: " + loginId, e);
        }
    }

    public void checkPassword(String password, Member member) {
        if (!passwordEncoder.matches(password, member.getPassword())) {
            log.info("비밀번호가 일치하지 않습니다. 입력 비밀번호 실제 비밀번호: {}", password, member.getPassword());
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.", password, member.getPassword());
        }
    }
}
