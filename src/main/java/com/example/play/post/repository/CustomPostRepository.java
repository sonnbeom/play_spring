package com.example.play.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomPostRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    public CustomPostRepository(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

}
