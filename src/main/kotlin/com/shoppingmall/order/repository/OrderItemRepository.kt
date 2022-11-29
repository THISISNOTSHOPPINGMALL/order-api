package com.shoppingmall.order.repository

import com.shoppingmall.order.domain.OrderItemEntity
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class OrderItemRepository(
    private val sessionFactory: Mutiny.SessionFactory
) {

    suspend fun save(itemOfOrder: OrderItemEntity): OrderItemEntity = itemOfOrder.also {
        sessionFactory.withSession { session ->
            if (it.orderItemId == null) { // save
                session.persist(it)
            } else { // update
                session.merge(it)
            }.flatMap { session.flush() }
        }.awaitSuspending()
    }

    suspend fun saveAll(itemOfOrderList: List<OrderItemEntity>): List<OrderItemEntity> = itemOfOrderList.also {
        sessionFactory.withSession { session ->
            session.persistAll(*itemOfOrderList.toTypedArray()).flatMap { session.flush() }
        }.awaitSuspending()
    }
}