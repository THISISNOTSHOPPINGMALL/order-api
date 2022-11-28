package com.shoppingmall.order.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.nullLiteral
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.shoppingmall.order.domain.CartEntity
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.springframework.stereotype.Repository

@Repository
class CartRepository(
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory
) {
    suspend fun save(cart: CartEntity): CartEntity =
        cart.also {
            queryFactory.transactionWithFactory { session, factory ->
                if (it.cartId == null) {
                    session.persist(cart)
                } else {
                    session.merge(cart)
                }
                    .flatMap { session.flush() }
                    .awaitSuspending()
            }
        }

    suspend fun findAllByUserId(userId: String, offset: Int, limit: Int): List<CartEntity> =
        queryFactory.listQuery {
            select(entity(CartEntity::class))
            from(CartEntity::class)
            where(
                and(
                    col(CartEntity::userId).equal(userId),
                    col(CartEntity::deletedAt).equal(nullLiteral())
                )
            )
            orderBy(col(CartEntity::createdAt).desc())
            limit(offset = offset, maxResults = limit)
        }

    suspend fun findByUserIdAndItemId(userId: String, itemId: Long): CartEntity? =
        queryFactory.singleQueryOrNull {
            select(entity(CartEntity::class))
            from(entity(CartEntity::class))
            where(
                and(
                    col(CartEntity::userId).equal(userId).and(col(CartEntity::itemId).equal(itemId)),
                    col(CartEntity::deletedAt).equal(nullLiteral())
                )
            )
        }

    suspend fun findByCartIdAndUserId(cartId: Long, userId: String): CartEntity? =
        queryFactory.singleQueryOrNull {
            select(entity(CartEntity::class))
            from(entity(CartEntity::class))
            where(
                and(
                    col(CartEntity::cartId).equal(cartId),
                    col(CartEntity::userId).equal(userId),
                    col(CartEntity::deletedAt).equal(nullLiteral())
                )
            )
        }

}
