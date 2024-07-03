package com.example.play.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;


@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public MemberCustomRepositoryImpl(EntityManager entityManager ) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


}
