package com.example.play.auth.service;

import com.example.play.auth.dto.*;
import com.example.play.member.domain.Member;
import com.example.play.member.repository.MemberRepository;
import com.example.play.member.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
           
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        String socialUserId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Member existMember = memberRepository.findBySocialUserId(socialUserId);

        if (existMember == null){
            Member member = Member.builder()
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .role(Role.ROLE_USER)
                    .socialUserId(socialUserId)
                    .build();

            memberRepository.save(member);
        }
        else {
            existMember.updateByOAuth(oAuth2Response.getEmail(), oAuth2Response.getName());
        }
        UserDto userDto = UserDto.builder().socialUserId(socialUserId).name(oAuth2Response.getName()).role("ROLE_USER").build();
        return new CustomOAuth2User(userDto);
    }
}
