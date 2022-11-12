package com.shoppingmall.order.domain

import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*


@Entity
@Table(name = "cart")
data class CartEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "user_id")
    val userId: String,
    @Column(name = "item_id")
    val itemId: Long,
    @Column(name = "amount")
    var amount: Int = 1,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
) {

    fun updateAmount(amount: Int): CartEntity {
        this.amount = amount
        return this
    }

}
