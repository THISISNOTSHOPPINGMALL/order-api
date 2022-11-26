package com.shoppingmall.order.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.nullLiteral
import com.linecorp.kotlinjdsl.spring.data.reactive.query.*
import com.shoppingmall.order.domain.CartEntity
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.stereotype.Repository

interface CartRepository {
    suspend fun create(cart: CartEntity): CartEntity
    suspend fun update(userId: String, itemId: Long, amount: Int): Int
    suspend fun findAllByUserId(userId: String, offset: Int, limit: Int): List<CartEntity>
    suspend fun findByUserIdAndItemId(userId: String, itemId: Long): CartEntity?

    suspend fun findByCartIdAndUserId(cartId: Long, userId: String): CartEntity?

    suspend fun delete(cartId: Long): Int
}

@Repository
class CartRepositoryImpl(
    private val sessionFactory: SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory
) : CartRepository {
    override suspend fun create(cart: CartEntity): CartEntity =
        cart.also {
            queryFactory.transactionWithFactory { session, factory ->
                session.persist(cart).awaitSuspending()
                session.flush().awaitSuspending()

//                factory.singleQuery<OrderEntity> {
//                    select(entity(OrderEntity::class))
//                    from(entity(OrderEntity::class))
//                    orderBy(col(OrderEntity::createdAt).desc())
//                    limit(1)
//                }
            }
        }

    override suspend fun update(userId: String, itemId: Long, amount: Int): Int = queryFactory.updateQuery<CartEntity> {
        where(
            and(
                col(CartEntity::userId).equal(userId),
                col(CartEntity::itemId).equal(itemId),
                col(CartEntity::deletedAt).equal(nullLiteral())
            )
        )
        set(col(CartEntity::amount), amount)
    }


    override suspend fun findAllByUserId(userId: String, offset: Int, limit: Int): List<CartEntity> =
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

    override suspend fun findByUserIdAndItemId(userId: String, itemId: Long): CartEntity? =
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

    override suspend fun findByCartIdAndUserId(cartId: Long, userId: String): CartEntity? =
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

    override suspend fun delete(cartId: Long): Int = queryFactory.deleteQuery<CartEntity> {
        where(
            col(CartEntity::cartId).equal(cartId)
        )
    }

}
