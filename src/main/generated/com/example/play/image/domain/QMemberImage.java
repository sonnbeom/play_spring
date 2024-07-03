package com.example.play.image.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberImage is a Querydsl query type for MemberImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberImage extends EntityPathBase<MemberImage> {

    private static final long serialVersionUID = -1027963565L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberImage memberImage = new QMemberImage("memberImage");

    public final com.example.play.global.common.entity.QBaseEntity _super = new com.example.play.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isActive = createNumber("isActive", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final com.example.play.member.domain.QMember member;

    public final StringPath url = createString("url");

    public QMemberImage(String variable) {
        this(MemberImage.class, forVariable(variable), INITS);
    }

    public QMemberImage(Path<? extends MemberImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberImage(PathMetadata metadata, PathInits inits) {
        this(MemberImage.class, metadata, inits);
    }

    public QMemberImage(Class<? extends MemberImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.play.member.domain.QMember(forProperty("member")) : null;
    }

}

