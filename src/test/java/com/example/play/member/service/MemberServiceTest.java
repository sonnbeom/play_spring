package com.example.play.member.service;

import com.example.play.image.service.MemberImgService;
import com.example.play.member.dto.RequestCreateMemberDto;
import com.example.play.member.entity.Member;
import com.example.play.member.memberMapper.MemberMapper;
import com.example.play.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberServiceImpl memberServiceImpl;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberImgService memberImgService;

    @Mock
    private MemberMapper memberMapper;

    @Test
    @DisplayName("서비스: 멤버 생성 테스트 프로필 존재")
    void memberCreateTestWithMemberImg(){
        //given
        RequestCreateMemberDto memberDto = new RequestCreateMemberDto("testName", "test@email.com", "testPwd", "testNickname");
        Member member = Member.builder()
                .email("test@email.com")
                .name("testName")
                .nickname("testNickname")
                .isActive(1)
                .password("testPwd")
                .build();

        Mockito.when(memberMapper.dtoToMember(Mockito.any(RequestCreateMemberDto.class))).thenReturn(member);
        Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenReturn(member);
        MockMultipartFile mockProfile = new MockMultipartFile("profile", "", "profile.jpg", "profile".getBytes());

        //when
        memberServiceImpl.createMember(memberDto, mockProfile);

        //then
        Mockito.verify(memberRepository, Mockito.times(1)).save(Mockito.any(Member.class));
        Mockito.verify(memberMapper, Mockito.times(1)).dtoToMember(Mockito.any(RequestCreateMemberDto.class));
        Mockito.verify(memberImgService, Mockito.times(1)).saveMemberImg(Mockito.any(Member.class), Mockito.any(MultipartFile.class));
    }
    @Test
    @DisplayName("서비스: 멤버 생성 테스트 프로필 X")
    void memberCreateTestWithoutMemberImg(){
        //given
        RequestCreateMemberDto memberDto = new RequestCreateMemberDto("testName", "test@email.com", "testPwd", "testNickname");
        Member member = Member.builder()
                .email("test@email.com")
                .name("testName")
                .nickname("testNickname")
                .isActive(1)
                .password("testPwd")
                .build();

        Mockito.when(memberMapper.dtoToMember(Mockito.any(RequestCreateMemberDto.class))).thenReturn(member);
        Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenReturn(member);
        MultipartFile profile = null;

        //when
        memberServiceImpl.createMember(memberDto, profile);

        //then
        Mockito.verify(memberRepository, Mockito.times(1)).save(Mockito.any(Member.class));
        Mockito.verify(memberMapper, Mockito.times(1)).dtoToMember(Mockito.any(RequestCreateMemberDto.class));
        Mockito.verify(memberImgService, Mockito.never()).saveMemberImg(Mockito.any(Member.class), Mockito.any(MultipartFile.class));
    }
}
