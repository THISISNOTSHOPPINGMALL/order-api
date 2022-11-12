package com.shoppingmall.order.repository

import com.shoppingmall.order.domain.CartEntity
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.linecorp.kotlinjdsl.spring.data.reactive.query.updateQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

interface CartRepository {
    suspend fun create(cart: CartEntity): CartEntity
    suspend fun update(userId: String, itemId: Long, amount: Int): Int
    suspend fun findByUserId(userId: String): List<CartEntity>
    suspend fun findByUserIdAndItemId(userId: String, itemId: Long): CartEntity?
}

@Repository
class CartRepositoryImpl(
    private val sessionFactory: SessionFactory,
    private val entityManager: EntityManager,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory
) : CartRepository {
    override suspend fun create(cart: CartEntity): CartEntity =
        cart.also {
            entityManager.persist(it)
//            sessionFactory.withSession { session -> session.persist(it).flatMap { session.flush() } }
//                .awaitSuspending()
        }

    override suspend fun update(userId: String, itemId: Long, amount: Int): Int = queryFactory.updateQuery<CartEntity> {
        where(
            and(
                col(CartEntity::userId).equal(userId),
                col(CartEntity::itemId).equal(itemId),
                col(CartEntity::deletedAt).equal(null)
            )
        )
        set(col(CartEntity::amount), amount)
    }


    override suspend fun findByUserId(userId: String): List<CartEntity> = queryFactory.listQuery {
        select(entity(CartEntity::class))
        from(entity(CartEntity::class))
        where(
            and(
                col(CartEntity::userId).equal(userId),
                col(CartEntity::deletedAt).equal(null)
            )
        )
    }

    override suspend fun findByUserIdAndItemId(userId: String, itemId: Long): CartEntity? =
        queryFactory.singleQueryOrNull {
            select(entity(CartEntity::class))
            from(entity(CartEntity::class))
            where(
                and(
                    col(CartEntity::userId).equal(userId).and(col(CartEntity::itemId).equal(itemId)),
                    col(CartEntity::deletedAt).equal(null)
                )
            )
        }

}
