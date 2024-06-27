package com.example.play.config;


import com.example.play.auth.service.CustomOAuth2UserService;
import com.example.play.jwt.filter.JwtTokenFilter;
import com.example.play.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final JwtService jwtService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                                return configuration;
                            }
                        }));
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
        http.
                formLogin((auth)-> auth.disable());
        http
                .httpBasic((auth) -> auth.disable());
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))));
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

        return http.build();
    }
}

