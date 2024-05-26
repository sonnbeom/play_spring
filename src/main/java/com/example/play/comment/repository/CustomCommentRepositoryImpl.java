package com.example.play.comment.repository;

import com.example.play.comment.domain.Comment;
import com.example.play.comment.domain.QComment;
import com.example.play.post.entity.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.play.comment.domain.QComment.*;

@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public CustomCommentRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Comment> getComments(Post post, Pageable pageable) {
        List<Comment>list = jpaQueryFactory
                .selectFrom(comment)
                .where(comment.post.eq(post))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> count = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.post.eq(post));
        return PageableExecutionUtils.getPage(list, pageable, count::fetchOne);
    }
}
