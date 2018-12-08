package org.claim.audit.vacation.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyOrder is a Querydsl query type for MyOrder
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMyOrder extends EntityPathBase<MyOrder> {

    private static final long serialVersionUID = -1944790789L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyOrder myOrder = new QMyOrder("myOrder");

    public final StringPath code = createString("code");

    public final QCustomer customer;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> total = createNumber("total", java.math.BigDecimal.class);

    public final NumberPath<Long> uId = createNumber("uId", Long.class);

    public QMyOrder(String variable) {
        this(MyOrder.class, forVariable(variable), INITS);
    }

    public QMyOrder(Path<? extends MyOrder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyOrder(PathMetadata metadata, PathInits inits) {
        this(MyOrder.class, metadata, inits);
    }

    public QMyOrder(Class<? extends MyOrder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.customer = inits.isInitialized("customer") ? new QCustomer(forProperty("customer")) : null;
    }

}

