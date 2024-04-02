package com.example.play.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -2041316198L;

    public static final QMember member = new QMember("member1");

    public final com.example.play.global.common.entity.QBaseEntity _super = new com.example.play.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final ListPath<com.example.play.friendship.entity.Friendship, com.example.play.friendship.entity.QFriendship> friendshipList = this.<com.example.play.friendship.entity.Friendship, com.example.play.friendship.entity.QFriendship>createList("friendshipList", com.example.play.friendship.entity.Friendship.class, com.example.play.friendship.entity.QFriendship.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isActive = createNumber("isActive", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final ListPath<com.example.play.image.entity.MemberImage, com.example.play.image.entity.QMemberImage> memberImages = this.<com.example.play.image.entity.MemberImage, com.example.play.image.entity.QMemberImage>createList("memberImages", com.example.play.image.entity.MemberImage.class, com.example.play.image.entity.QMemberImage.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<com.example.play.like.post.entity.PostLike, com.example.play.like.post.entity.QPostLike> postLikes = this.<com.example.play.like.post.entity.PostLike, com.example.play.like.post.entity.QPostLike>createList("postLikes", com.example.play.like.post.entity.PostLike.class, com.example.play.like.post.entity.QPostLike.class, PathInits.DIRECT2);

    public final ListPath<com.example.play.post.entity.Post, com.example.play.post.entity.QPost> postList = this.<com.example.play.post.entity.Post, com.example.play.post.entity.QPost>createList("postList", com.example.play.post.entity.Post.class, com.example.play.post.entity.QPost.class, PathInits.DIRECT2);

    public final EnumPath<com.example.play.member.role.Role> role = createEnum("role", com.example.play.member.role.Role.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

