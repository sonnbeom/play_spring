package com.example.play.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 707830181L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.example.play.global.common.entity.QBaseEntity _super = new com.example.play.global.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.play.image.domain.PostImage, com.example.play.image.domain.QPostImage> imageList = this.<com.example.play.image.domain.PostImage, com.example.play.image.domain.QPostImage>createList("imageList", com.example.play.image.domain.PostImage.class, com.example.play.image.domain.QPostImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> isActive = createNumber("isActive", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final com.example.play.member.domain.QMember member;

    public final ListPath<com.example.play.like.post.domain.PostLike, com.example.play.like.post.domain.QPostLike> postLikes = this.<com.example.play.like.post.domain.PostLike, com.example.play.like.post.domain.QPostLike>createList("postLikes", com.example.play.like.post.domain.PostLike.class, com.example.play.like.post.domain.QPostLike.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.play.member.domain.QMember(forProperty("member")) : null;
    }

}

