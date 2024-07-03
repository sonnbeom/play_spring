package com.example.play.member.domain;

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

    private static final long serialVersionUID = 358120601L;

    public static final QMember member = new QMember("member1");

    public final com.example.play.global.common.entity.QBaseEntity _super = new com.example.play.global.common.entity.QBaseEntity(this);

    public final ListPath<com.example.play.comment.domain.Comment, com.example.play.comment.domain.QComment> commentList = this.<com.example.play.comment.domain.Comment, com.example.play.comment.domain.QComment>createList("commentList", com.example.play.comment.domain.Comment.class, com.example.play.comment.domain.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isActive = createNumber("isActive", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final ListPath<com.example.play.image.domain.MemberImage, com.example.play.image.domain.QMemberImage> memberImages = this.<com.example.play.image.domain.MemberImage, com.example.play.image.domain.QMemberImage>createList("memberImages", com.example.play.image.domain.MemberImage.class, com.example.play.image.domain.QMemberImage.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<com.example.play.like.post.domain.PostLike, com.example.play.like.post.domain.QPostLike> postLikes = this.<com.example.play.like.post.domain.PostLike, com.example.play.like.post.domain.QPostLike>createList("postLikes", com.example.play.like.post.domain.PostLike.class, com.example.play.like.post.domain.QPostLike.class, PathInits.DIRECT2);

    public final ListPath<com.example.play.post.domain.Post, com.example.play.post.domain.QPost> postList = this.<com.example.play.post.domain.Post, com.example.play.post.domain.QPost>createList("postList", com.example.play.post.domain.Post.class, com.example.play.post.domain.QPost.class, PathInits.DIRECT2);

    public final ListPath<com.example.play.friendship.domain.Friendship, com.example.play.friendship.domain.QFriendship> receivedFriendships = this.<com.example.play.friendship.domain.Friendship, com.example.play.friendship.domain.QFriendship>createList("receivedFriendships", com.example.play.friendship.domain.Friendship.class, com.example.play.friendship.domain.QFriendship.class, PathInits.DIRECT2);

    public final EnumPath<com.example.play.member.role.Role> role = createEnum("role", com.example.play.member.role.Role.class);

    public final ListPath<com.example.play.friendship.domain.Friendship, com.example.play.friendship.domain.QFriendship> sentFriendships = this.<com.example.play.friendship.domain.Friendship, com.example.play.friendship.domain.QFriendship>createList("sentFriendships", com.example.play.friendship.domain.Friendship.class, com.example.play.friendship.domain.QFriendship.class, PathInits.DIRECT2);

    public final StringPath socialUserId = createString("socialUserId");

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

