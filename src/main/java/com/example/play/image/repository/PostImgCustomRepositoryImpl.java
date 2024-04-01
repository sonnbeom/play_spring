package com.example.play.image.repository;

import com.example.play.image.entity.PostImage;
import com.example.play.image.entity.QPostImage;
import com.example.play.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.play.image.entity.QPostImage.postImage;

@Repository
public class PostImgCustomRepositoryImpl implements PostImgCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    public PostImgCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PostImage> readByPost(Post post) {
        return jpaQueryFactory.
                selectFrom(postImage)
                .where(postImage.post.eq(post)
                        .and(postImage.isActive.eq(1)))
                .fetch();
    }

    @Override
    public List<PostImage> findListForDelete(Post post, List<Long> deleteIds) {
       return jpaQueryFactory
               .selectFrom(postImage)
               .where(postImage.post.eq(post)
                       .and(postImage.id.in(deleteIds))
                       .and(postImage.isActive.eq(1)))
               .fetch();
    }
}
