package com.shoppingmall.order.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.nullLiteral
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.shoppingmall.order.domain.OrderItemEntity
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class OrderItemRepository(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory
) {

    suspend fun create(itemOfOrder: OrderItemEntity): OrderItemEntity = itemOfOrder.also {
        sessionFactory.withSession { session -> session.persist(it).flatMap { session.flush() } }
            .awaitSuspending()
    }

    suspend fun findAllByOrderId(orderId: Long): List<OrderItemEntity> = queryFactory.listQuery {
        select(entity(OrderItemEntity::class))
        from(OrderItemEntity::class)
        where(
            and(
                col(OrderItemEntity::orderId).equal(orderId),
                col(OrderItemEntity::deletedAt).equal(nullLiteral())
            )
        )
        orderBy(col(OrderItemEntity::createdAt).desc())
    }
}