package com.shoppingmall.order.dto

import com.shoppingmall.order.domain.OrderItemEntity

class OrderItemDto {

    class Request {
        data class Add(
            val itemId: Long,
            val price: Long,
            val amount: Long
        )
    }

    class Response {

        data class Simple(
            val OrderItemId: Long,
            val orderId: Long,
            val itemId: Long,
            val price: Long,
            val amount: Long
        ) {
            companion object {
                fun from(entity: OrderItemEntity): Simple = Simple(
                    OrderItemId = entity.orderItemId!!,
                    orderId = entity.orderId,
                    itemId = entity.itemId,
                    price = entity.price,
                    amount = entity.amount
                )


            }
        }
    }
}