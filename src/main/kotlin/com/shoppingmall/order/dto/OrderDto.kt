package com.shoppingmall.order.dto

import com.shoppingmall.order.domain.OrderEntity
import com.shoppingmall.order.domain.OrderStatus
import java.time.LocalDateTime

class OrderDto {

    class Request {

        data class Add(
            val address: String,
            val itemIdList: List<OrderItemDto.Request.Add>
        )

    }

    class Response {

        data class Simple(
            val orderId: Long,
            val userId: String,
            val status: OrderStatus,
            val address: String,
            val logisId: Long?,
            val waybillNum: String?,
            val paymentId: Long?,
            val createdAt: LocalDateTime,
            val itemList: List<OrderItemDto.Response.Simple>
        ) {
            companion object {
                fun from(order: OrderEntity) = Simple(
                    orderId = order.orderId!!,
                    userId = order.userId,
                    status = order.status,
                    address = order.address,
                    logisId = order.logisId,
                    waybillNum = order.waybillNum,
                    paymentId = order.paymentId,
                    createdAt = order.createdAt,
                    itemList = order.items.map { OrderItemDto.Response.Simple.from(it) }
                )
            }
        }
    }
}