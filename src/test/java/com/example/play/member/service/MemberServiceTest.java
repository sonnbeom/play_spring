package com.example.play.member.service;

import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.service.MemberImgService;
import com.example.play.member.dto.RequestCreateMemberDto;
import com.example.play.member.dto.RequestUpdateMemberDto;
import com.example.play.member.dto.ResponseDeleteMemberDto;
import com.example.play.member.dto.ResponseMemberDto;
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

import java.util.Optional;

import static com.example.play.member.dto.ResponseDeleteMemberDto.STATUS.*;
import static org.junit.jupiter.api.Assertions.*;


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
    @DisplayName("멤버 서비스: 멤버 생성 테스트 프로필 존재")
    void memberCreateTestWithMemberImg(){
        //given
        RequestCreateMemberDto memberDto = new RequestCreateMemberDto("testName", "test@email.com", "testPwd", "testNickname");
        Member member = getTestMember();

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
    @DisplayName("멤버 서비스: 멤버 생성 테스트 프로필 X")
    void memberCreateTestWithoutMemberImg(){
        //given
        RequestCreateMemberDto memberDto = new RequestCreateMemberDto("testName", "test@email.com", "testPwd", "testNickname");
        Member member = getTestMember();

        Mockito.when(memberMapper.dtoToMember(Mockito.any(RequestCreateMemberDto.class))).thenReturn(member);
        Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenReturn(member);
        MultipartFile profile = null;

        //when
        memberServiceImpl.createMember(memberDto, profile);

        //then
        Mockito.verify(memberRepository, Mockito.times(1)).save(member);
        Mockito.verify(memberMapper, Mockito.times(1)).dtoToMember(memberDto);
        Mockito.verify(memberImgService, Mockito.never()).saveMemberImg(member, profile);
    }
    @Test
    @DisplayName("멤버 서비스: 단일 멤버 가져오기")
    void getMemberTest(){
        //given
        String testEmail = "test@email.com";
        Long testMemberId = 1L;
        Member testMember = getTestMember();
        ResponseMemberImg responseMemberImg = getTestResponseMemberImg();

        Mockito.when(memberRepository.findByEmail(testEmail)).thenReturn(Optional.of(testMember));
        Mockito.when(memberImgService.findByMember(testMember)).thenReturn(responseMemberImg);

        //when
        ResponseMemberDto expectedDto = memberServiceImpl.getMember(testEmail, testMemberId);

        //then
        Mockito.verify(memberRepository).findByEmail(testEmail);
        Mockito.verify(memberImgService).findByMember(testMember);
        assertEquals(testEmail, expectedDto.getEmail());
        assertEquals("testName", expectedDto.getName());
        assertEquals("testNickname", expectedDto.getNickname());
        assertEquals(responseMemberImg, expectedDto.getImg());
    }
    @Test
    @DisplayName("멤버 서비스: 멤버 업데이트")
    void updateMemberTest(){
        //given
        String testEmail = "test@email.com";
        RequestUpdateMemberDto updateMemberDto = Mockito.mock(RequestUpdateMemberDto.class);
        Member member = Mockito.mock(Member.class);
        ResponseMemberImg responseImg = getTestResponseMemberImg();
        Long deleteFileId = 1L;
        MockMultipartFile updateProfile = new MockMultipartFile("profile", "", "profile.jpg", "profile".getBytes());

        Mockito.when(memberRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(member));
        Mockito.when(updateMemberDto.isUpdateNicknamePresent()).thenReturn(true);
        Mockito.when(updateMemberDto.isUpdatePwdPresent()).thenReturn(true);
        Mockito.when(updateMemberDto.isUpdateEmailPresent()).thenReturn(true);
        Mockito.when(memberImgService.updateStatus(updateProfile, deleteFileId, member)).thenReturn(responseImg);
        Mockito.when(member.toDto(responseImg)).thenReturn(new ResponseMemberDto(1L, "newNickName", "newEmail", "newNickname",responseImg));


        //when
        ResponseMemberDto result = memberServiceImpl.updateMember(testEmail, updateMemberDto, updateProfile, deleteFileId);

        //then
        assertNotNull(result);
        assertEquals("newNickname", result.getNickname());
        Mockito.verify(memberImgService).updateStatus(updateProfile, deleteFileId, member);
        Mockito.verify(updateMemberDto).sendUpdateEmailToMember(member);
        Mockito.verify(updateMemberDto).sendNicknameToMember(member);
        Mockito.verify(updateMemberDto).sendUpdatePwdToMember(member);


    }
    @Test
    @DisplayName("멤버 서비스: 삭제 테스트")
    void deleteMemberTest(){
        //given
        String testEmail = "test@email.com";
        Long testMemberId = 1L;
        Member mockMember = Mockito.mock(Member.class);
        ResponseDeleteMemberDto responseDeleteMemberDto = new ResponseDeleteMemberDto(DELETED, DELETED);

        Mockito.when(memberRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(mockMember));
        Mockito.doNothing().when(mockMember).checkDeleteAuthority(testMemberId);
        Mockito.when(memberImgService.delete(mockMember)).thenReturn(0);
        Mockito.when(mockMember.changeStatus()).thenReturn(0);
        Mockito.when(memberMapper.deleteResponse(0,0)).thenReturn(responseDeleteMemberDto);

        //when
        ResponseDeleteMemberDto result = memberServiceImpl.deleteMember(testEmail, testMemberId);

        //then
        Mockito.verify(mockMember).checkDeleteAuthority(testMemberId);
        Mockito.verify(mockMember).changeStatus();
        Mockito.verify(memberImgService).delete(mockMember);
        assertNotNull(result);
        assertEquals(result.getMemberStatus(), DELETED);
        assertEquals(result.getMemberImgStatus(), DELETED);
        assertEquals(responseDeleteMemberDto ,result);
    }
    private Member getTestMember(){
        return Member.builder()
                .email("test@email.com")
                .name("testName")
                .nickname("testNickname")
                .isActive(1)
                .password("testPwd")
                .build();
    }
    private ResponseMemberImg getTestResponseMemberImg(){
        return ResponseMemberImg.builder()
                .id(1L)
                .url("testUrl")
                .status(ResponseMemberImg.Status.NOT_DEFAULT)
                .build();
    }
}
