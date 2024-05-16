package com.example.play.member.controller;

import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.member.constant.TestMemberUrl;
import com.example.play.member.dto.RequestCreateMemberDto;
import com.example.play.member.dto.RequestUpdateMemberDto;
import com.example.play.member.dto.ResponseMemberDto;
import com.example.play.member.service.MemberService;
import com.example.play.mock.WithCustomMockUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import static com.example.play.member.constant.TestMemberUrl.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @Autowired
    private WebApplicationContext context;
    // 테스트 환경이 실제 애플리케이션의 스프링 컨텍스트 설정을 반영할 수 있음
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    //테스트 환경이 스프링 컨텍스르를 반영 => 필터, 인터셉터 등 다양한 요소를 포함 가능하게 만듦


    private ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @DisplayName("컨트롤러: 멤버 생성 테스트")
    @WithMockUser
    void testMemberCreate() throws Exception {
        // given
        MockMultipartFile memberDtoPart = getMemberDtoPart();
        MockMultipartFile profile = getFile();
        doNothing().when(memberService).createMember(any(RequestCreateMemberDto.class), any(MultipartFile.class));
        //do Nothing은 void 메소드를 모의 처리할 때 사용하는 메소드 when.thenReturn()을 사용하지만 void인 경우 doNothing을 사용해 처리

        /*
        when then
        */
        mockMvc.perform(MockMvcRequestBuilders.multipart(JOIN_URL)
                        .file(memberDtoPart)
                        .file(profile)
                        .with(csrf()))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @WithCustomMockUser
    @DisplayName("컨트롤러 단일 멤버 불러오기")
    void testGetMember() throws Exception {
        //given
        ResponseMemberDto dto = new ResponseMemberDto(1L, "test name", "test@email.com", "test nickname", new ResponseMemberImg(ResponseMemberImg.Status.DEFAULT));
        Mockito.when(memberService.getMember("test@email.com")).thenReturn(dto);

        /*
        when then
        */
        mockMvc.perform(MockMvcRequestBuilders.get(GET_MEMBER_URL, 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("test nickname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.img.status").value("DEFAULT"));
    }
    @Test
    @DisplayName("컨트롤러 멤버 정보 수정하기")
    @WithCustomMockUser
    void testMemberUpdate() throws Exception {
        //given

        //수정하고자 하는 멤버 데이터
        RequestUpdateMemberDto updateMemberDto = new RequestUpdateMemberDto("test nickname", "test@email.com", "test pwd");
        MockMultipartFile updateMemberDtoPart = getUpdateMemberDtoPart(updateMemberDto);
        // 수정하고자 하는 멤버 이미지
        MockMultipartFile updateFile = new MockMultipartFile("file", "", "image/jpeg", "JPG content".getBytes());
        // 삭제하고자 하는 멤버 이미지
        MockMultipartFile deleteFile = new MockMultipartFile("deleteFile", "", "application/json", "1".getBytes());

        ResponseMemberDto responseMemberDto = new ResponseMemberDto(1L,"name", "test@email.com", "test nickname", new ResponseMemberImg(ResponseMemberImg.Status.NOT_DEFAULT));
        Mockito.when(memberService.updateMember(Mockito.anyString(), any(RequestUpdateMemberDto.class), any(MultipartFile.class), Mockito.anyLong())).thenReturn(responseMemberDto);

       /*
        when then
        */
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH ,PATCH_MEMBER_URL, 1L)
                .file(updateMemberDtoPart)
                .file(updateFile)
                .file(deleteFile)
                .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("test nickname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.img.status").value("NOT_DEFAULT"));

    }

    private MockMultipartFile  getMemberDtoPart() throws JsonProcessingException {
        RequestCreateMemberDto memberDto = new RequestCreateMemberDto("test name", "test email", "test pwd", "test nickname");
        MockMultipartFile  memberDtoPart =  new MockMultipartFile("memberDto","memberDto", "application/json", objectMapper.writeValueAsBytes(memberDto));
        return memberDtoPart;
    }
    private MockMultipartFile getFile(){
        return new MockMultipartFile("profile", "filename.jpg", "image/jpeg", "JPG content".getBytes());
    }
    private MockMultipartFile getUpdateMemberDtoPart(RequestUpdateMemberDto dto) throws JsonProcessingException {
        return new MockMultipartFile("updateDto", "", "application/json", objectMapper.writeValueAsBytes(dto));
    }
}