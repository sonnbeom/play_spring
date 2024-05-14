package com.example.play.mock;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
// JVM이 실행 시점에도 이 정보를 읽을 수 있게. 동적으로 처리할 작업을 수행하기 때문
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
//스프링 시큐리티를 커스텀하기 위함. CustomMockUserSecurityContextFactory에서 어노테이션을 활용할 수 있음
public @interface WithCustomMockUser {
    String email() default "test@email.com";
}
