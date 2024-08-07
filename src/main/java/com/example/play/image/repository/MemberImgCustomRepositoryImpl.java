package com.example.play.image.repository;

import com.example.play.image.domain.MemberImage;
import com.example.play.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.play.image.domain.QMemberImage.memberImage;

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

    @Override
    public List<MemberImage> findByIdAndMember(Long id, Member member) {
        return jpaQueryFactory
                .selectFrom(memberImage)
                .where(memberImage.member.eq(member)
                        .and(memberImage.id.eq(id))
                        .and(memberImage.isActive.eq(1)))
                .fetch();
    }

    @Override
    public List<MemberImage> findImgsByMemberList(List<Member> memberList) {
        return jpaQueryFactory
                .selectFrom(memberImage)
                .innerJoin(memberImage.member)
                .where(memberImage.isActive.eq(1)
                        .and(memberImage.member.in(memberList)))
                .fetch();
    }

}
