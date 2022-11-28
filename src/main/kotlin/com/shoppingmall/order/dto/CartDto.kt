package com.shoppingmall.order.dto

import com.shoppingmall.order.domain.CartEntity

class CartDto {

    class Request {
        data class Add(
            val userId: Long,
            val itemId: Long,
            val amount: Int
        )
    }

    class Response {
        data class Simple(
            val cartId: Long,
            val userId: String,
            val itemId: Long,
            val amount: Int
        ) {
            companion object {
                fun from(cart: CartEntity): Simple =
                    Simple(cartId = cart.cartId, userId = cart.userId, itemId = cart.itemId, amount = cart.amount)
            }
        }
    }
}