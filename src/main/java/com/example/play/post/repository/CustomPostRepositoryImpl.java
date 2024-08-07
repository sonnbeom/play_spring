package com.example.play.post.repository;

import com.example.play.member.domain.Member;
import com.example.play.post.constant.PostSearchType;
import com.example.play.post.constant.PostSortType;
import com.example.play.post.domain.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.play.like.post.domain.QPostLike.*;
import static com.example.play.member.domain.QMember.*;
import static com.example.play.post.domain.QPost.*;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    public CustomPostRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    //검색

    @Override
    public Page<Post> findBySort(Pageable pageable, String type) {
        PostSortType sortType = PostSortType.valueOf(type.toUpperCase());
        OrderSpecifier<?> orderBy = null;

        switch (sortType){
            case HIT :
                orderBy = post.hit.desc();
                break;
            case LIKE:
                orderBy = post.likeCount.desc();
                break;
            case LATEST:
                orderBy = post.createdAt.desc();
                break;
        }

        List<Post> posts = jpaQueryFactory.
                selectFrom(post)
                .where(post.isActive.eq(1))
                .orderBy(orderBy)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = jpaQueryFactory
                .selectFrom(post)
                .where(post.isActive.eq(1))
                .fetchCount();
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findBySearch(Pageable pageable ,String type, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.isActive.eq(1));

        PostSearchType searchType = PostSearchType.valueOf(type.toUpperCase());
        OrderSpecifier orderBy = null;

        switch (searchType){
            case TITLE :
                builder.and(post.title.eq(keyword));
                break;
            case NICKNAME:
                builder.and(member.nickname.eq(keyword));
                break;
            case CONTENT:
                builder.and(post.content.like(keyword));
                break;
        }
        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.member, member)
                .where(builder)
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = jpaQueryFactory
                .selectFrom(post)
                .where(builder)
                .fetchCount();
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findLikedPosts(Member member, Pageable pageable) {

        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .innerJoin(post.postLikes, postLike)
                .where(postLike.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(post.count())
                .from(post)
                .innerJoin(post.postLikes, postLike)
                .where(postLike.member.eq(member));

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }
}
