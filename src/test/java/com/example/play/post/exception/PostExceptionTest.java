package com.example.play.post.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostExceptionTest {
    @DisplayName("게시글이 없을 때 해당 에러와 404 status를 리턴하는지 테스트")
    @Test
    void postNotFoundException(){

    }
}
