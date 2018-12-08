package org.claim.audit.common.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserGroup is a Querydsl query type for UserGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserGroup extends EntityPathBase<UserGroup> {

    private static final long serialVersionUID = 2034796027L;

    public static final QUserGroup userGroup = new QUserGroup("userGroup");

    public final StringPath id = createString("id");

    public final StringPath name = createString("name");

    public final StringPath status = createString("status");

    public final ListPath<User, QUser> userList = this.<User, QUser>createList("userList", User.class, QUser.class, PathInits.DIRECT2);

    public QUserGroup(String variable) {
        super(UserGroup.class, forVariable(variable));
    }

    public QUserGroup(Path<? extends UserGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserGroup(PathMetadata metadata) {
        super(UserGroup.class, metadata);
    }

}

