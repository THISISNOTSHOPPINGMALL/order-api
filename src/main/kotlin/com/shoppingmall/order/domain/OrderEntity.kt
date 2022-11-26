package com.shoppingmall.order.domain

import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*


enum class OrderStatus {
    BEFORE_PAYMENT, // 입금 전
    PAYMENT_CONFIRMED, // 입금 확인
    CANCELLED_BEFORE_PAYMENT, // 입금 전 취소
    PACKING_FOR_SHIPMENT, // 배송 준비중
    CANCELLED_BEFORE_SHIPMENT, // 배송 전 취소
    ON_DELIVERY, // 배송 중
    DELIVERY_OVER, // 배송 완료
    ON_RETURN, // 반품 반송중
    RETURN_OVER // 반품 완료
}

@Entity
@Table(name = "order_t")
data class OrderEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    val orderId: Long? = null,
    @Column(name = "user_id")
    val userId: String,

    @Column(name = "status")
    val status: OrderStatus,
    @Column(name = "address")
    val address: String,

    @Column(name = "logis_id")
    var logisId: Long? = null,
    @Column(name = "waybill_num")
    var waybillNum: String? = null,
    @Column(name = "payment_id")
    var paymentId: Long? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,

    @OneToMany
    @JoinColumn(name = "order_id")
    val items: List<OrderItemEntity> = emptyList()
)