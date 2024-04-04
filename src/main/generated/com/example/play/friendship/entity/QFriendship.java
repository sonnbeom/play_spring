package com.example.play.friendship.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendship is a Querydsl query type for Friendship
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendship extends EntityPathBase<Friendship> {

    private static final long serialVersionUID = -1369532774L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendship friendship = new QFriendship("friendship");

    public final com.example.play.global.common.entity.QBaseEntity _super = new com.example.play.global.common.entity.QBaseEntity(this);

    public final NumberPath<Long> counterpartId = createNumber("counterpartId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath friendEmail = createString("friendEmail");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isFrom = createBoolean("isFrom");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final com.example.play.member.entity.QMember member;

    public final StringPath memberEmail = createString("memberEmail");

    public final EnumPath<com.example.play.friendship.constant.FriendshipStatus> status = createEnum("status", com.example.play.friendship.constant.FriendshipStatus.class);

    public QFriendship(String variable) {
        this(Friendship.class, forVariable(variable), INITS);
    }

    public QFriendship(Path<? extends Friendship> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendship(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendship(PathMetadata metadata, PathInits inits) {
        this(Friendship.class, metadata, inits);
    }

    public QFriendship(Class<? extends Friendship> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.play.member.entity.QMember(forProperty("member")) : null;
    }

}

