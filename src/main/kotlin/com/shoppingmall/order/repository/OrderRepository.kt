package com.shoppingmall.order.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.nullLiteral
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.shoppingmall.order.domain.OrderEntity
import com.shoppingmall.order.domain.OrderItemEntity
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.springframework.stereotype.Repository


@Repository
class OrderRepository(
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory
) {
    suspend fun create(order: OrderEntity): OrderEntity = order.also {
        queryFactory.withFactory { session, factory ->
            session.persist(order).awaitSuspending()
            session.flush().awaitSuspending()
        }
    }

    suspend fun findByOrderIdAndUserId(orderId: Long, userId: String): OrderEntity? = queryFactory.singleQueryOrNull {
        select(entity(OrderEntity::class))
        from(OrderEntity::class)
        fetch(OrderEntity::class, OrderItemEntity::class, on(OrderEntity::items))
        where(
            and(
                col(OrderEntity::orderId).equal(orderId),
                col(OrderEntity::userId).equal(userId),
                col(OrderEntity::deletedAt).equal(nullLiteral())
            )
        )
    }

    suspend fun findAllByUserId(userId: String, limit: Int, offset: Int): List<OrderEntity> =
        queryFactory.listQuery {
            select(entity(OrderEntity::class))
            from(OrderEntity::class)
            fetch(OrderEntity::class, OrderItemEntity::class, on(OrderEntity::items))
            where(
                and(
                    col(OrderEntity::userId).equal(userId),
                    col(OrderEntity::deletedAt).equal(nullLiteral())
                )
            )
            orderBy(
                col(OrderEntity::createdAt).desc()
            )
            limit(offset, limit)
        }


}