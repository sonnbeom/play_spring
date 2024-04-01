package com.example.play.image.repository;

import com.example.play.image.entity.MemberImage;
import com.example.play.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.play.image.entity.QMemberImage.memberImage;

@Repository
public class MemberImgCustomRepositoryImpl implements MemberImgCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    public MemberImgCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<MemberImage> findByMember(Member member) {
        return jpaQueryFactory
                .selectFrom(memberImage)
                .where(memberImage.member.eq(member)
                        .and(memberImage.isActive.eq(1)))
                .fetch();
    }
}
