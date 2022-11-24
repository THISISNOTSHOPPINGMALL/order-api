package com.shoppingmall.order.dto

import com.shoppingmall.order.domain.ItemOfOrderEntity

class ItemOfOrderDto {

    class Response {

        data class Simple(
            val itemOfOrderId: Long,
            val orderId: Long,
            val itemId: Long,
            val price: Long,
            val amount: Long
        ) {
            companion object {
                fun from(entity: ItemOfOrderEntity): Simple = Simple(
                    itemOfOrderId = entity.itemOfOrderId,
                    orderId = entity.orderId,
                    itemId = entity.itemId,
                    price = entity.price,
                    amount = entity.amount
                )


            }
        }
    }
}