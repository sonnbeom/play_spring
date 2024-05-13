package com.example.play.member.repository;

import com.example.play.member.entity.Member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public MemberCustomRepositoryImpl(EntityManager entityManager ) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


}
