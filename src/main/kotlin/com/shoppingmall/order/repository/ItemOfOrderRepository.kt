package com.shoppingmall.order.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.nullLiteral
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.shoppingmall.order.domain.ItemOfOrderEntity
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class ItemOfOrderRepository(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory
) {

    suspend fun create(itemOfOrder: ItemOfOrderEntity): ItemOfOrderEntity = itemOfOrder.also {
        sessionFactory.withSession { session -> session.persist(it).flatMap { session.flush() } }
            .awaitSuspending()
    }

    suspend fun findAllByOrderId(orderId: Long): List<ItemOfOrderEntity> = queryFactory.listQuery {
        select(entity(ItemOfOrderEntity::class))
        from(ItemOfOrderEntity::class)
        where(
            and(
                col(ItemOfOrderEntity::orderId).equal(orderId),
                col(ItemOfOrderEntity::deletedAt).equal(nullLiteral())
            )
        )
        orderBy(col(ItemOfOrderEntity::createdAt).desc())
    }
}