package com.example.play.auth.service;

import com.example.play.auth.dto.OAuthAttributes;
import com.example.play.auth.dto.SessionMember;
import com.example.play.member.entity.Member;
import com.example.play.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
// OAuth2UserService<OAuth2UserRequest, OAuth2User> 를 사용함으로써 사용자 정보를 로드하는 역할을 한다
// OAuth2UserRequest(사용자 정보 요청)을 처리하고 OAuth2User 객체를 반환한다.
public class CustomAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        // OAuth2UserService의 구현체가 DefaultOAuth2UserService이다.
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        // OAuth2UserService 인터페이스에서 정의한 메소드로 OAuth2UserRequest객체를 인자로 받아 해당 요청을 처리한 후 OAuth2User 객체를 반환한다.
        // 사용자의 이름 이메일을 가져오는 역할을 함
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //registrationId는 OAuth2 제공자를 식별하는 고유한 문자열 현재 로그인 시도 중인 OAuth2 제공자의 식별자를 가져온다.
        //registrationId = 여러 소셜 로그인 서비스를 연동할 때 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        //userNameAttributeName OAuth2 로그인 진행시 키가 되는 필드 값 PK
        // OAuth2 로그인 과정에서 얻은 사용자 정보 OAuth2User.getAttributes를 바탕으로 애플리케이션에서 사용할 정보 객체인 OAuthAttributes를 생성
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);
        httpSession.setAttribute("member", member.memberToSessionMember());
        // 애플리케이션에서 사용할 OAuth2User 객체를 생성하여 반환 사용자의 권한 정보(key),  속성 정보(attributes), 식별자의 키(getNameAttributeKey)
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleForToken().toString())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }
    private Member saveOrUpdate(OAuthAttributes attributes){
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());
        return memberRepository.save(member);
    }
}
