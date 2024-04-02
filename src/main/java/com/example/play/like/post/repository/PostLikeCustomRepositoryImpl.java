package com.example.play.like.post.repository;

import com.example.play.like.post.entity.PostLike;
import com.example.play.like.post.entity.QPostLike;
import com.example.play.member.entity.Member;
import com.example.play.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.play.like.post.entity.QPostLike.*;

@Repository
public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public PostLikeCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PostLike> findByPostAndMember(Post post, Member member) {
        return jpaQueryFactory
                .selectFrom(postLike)
                .where(postLike.post.eq(post)
                        .and(postLike.member.eq(member))
                        .and(postLike.isActive.eq(1)))
                .fetch();
    }
}
