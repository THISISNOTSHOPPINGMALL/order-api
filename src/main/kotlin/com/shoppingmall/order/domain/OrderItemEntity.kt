package com.shoppingmall.order.domain

import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*

@Entity
@Table(name = "item_of_order")
data class OrderItemEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    val orderItemId: Long? = null,

    @Column(name = "order_id")
    val orderId: Long,
    @Column(name = "item_id")
    val itemId: Long,
    @Column(name = "price")
    val price: Long,
    @Column(name = "amount")
    val amount: Long,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
)