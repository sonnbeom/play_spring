package com.example.play.config;

import com.example.play.auth.service.CustomAuth2UserService;
import com.example.play.jwt.filter.JwtTokenFilter;

import com.example.play.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final CustomAuth2UserService customAuth2UserService;
    private final JwtService jwtService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        OAuth2LoginConfigurer<HttpSecurity> HttpSecurity;
        http
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .addFilterBefore(new JwtTokenFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        http
                // CSRF 설정 변경
                .csrf(csrf -> csrf.disable());
        http
                .headers(headers -> headers.disable());
        http
                .authorizeRequests(auth -> auth
                        // 루트(/) , css, 이미지 ,자바스크립트 파일, h2 콘솔 경로에 대한 요청은 인증 없이 접근을 허용
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                        // 해당 경로로 들어오는 요청은 user 역할을 가진 사용자만 접근할 수 있음
                        .requestMatchers(HttpMethod.POST,"/api/v1/member").permitAll()
                        .requestMatchers("/api/v1/member/login").permitAll()
                        .requestMatchers("/api/v1/auth/authenticate").permitAll()
                        .requestMatchers("/api/v1/auth/reissue").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/v1/comment").permitAll()
                        .requestMatchers("/api/v1/member/admin").hasRole("ADMIN")
                        // 위에서 정의하지 않은 요청은 인증된 사용자만 접근할 수 있음
                        .anyRequest().authenticated());
//        http
//                .logout(logout -> logout
//                        //로그아웃 기능을 활성화하고 로그 아웃 성공시 사용자를 해당 url로 리다이렉트한다
//                        .logoutSuccessUrl("/"));
//        http
//                .formLogin(form -> form
//                        //form 기반의 인증을 활성화 사용자가 폼을 통해 로그인할 수 있게
//                        .loginPage("/member/login")
//                        // 사용자 정의 로그인 페이지의 로그인을 설정
//                        .defaultSuccessUrl("/")
//                        // 성공 후 리다이렉트될 기본 url을 설정합니다.
//                        .usernameParameter("userId")
//                        // 로그인 폼에서 사용자 이름과 비밀번호 필드의 이름 설정
//                        .passwordParameter("password")
//                        // 로그인 폼이 제출될 url을 설정합니다. 이 url로 post 요청이 들어오면 시큐리티가 사용자 인증을 처리합니다. 설정된 url은 폼 액션에 명시해야합니다.ㄷ
//                        .loginProcessingUrl("/login"))
//                // OAuth2 로그인을 활성화합니다.
//                .oauth2Login(oauth2Login
//                        -> oauth2Login.userInfoEndpoint(userInfoEndpointConfig
//                        -> userInfoEndpointConfig.userService(customAuth2UserService)));
        return http.build();
    }
}

