package com.shoppingmall.order.service

import com.shoppingmall.order.domain.CartEntity
import com.shoppingmall.order.dto.CartDto
import com.shoppingmall.order.repository.CartRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class CartService(
    private val cartRepository: CartRepository,
) {

    @Transactional
    suspend fun addItemToCart(userId: String, req: CartDto.Request.Add): CartDto.Response.Simple {
        // TODO: ItemId, UserId 검증 로직

        return (
//                cartRepository.findByUserIdAndItemId(userId = userId, itemId = req.itemId)
//                    ?.also { cartRepository.update(userId = userId, itemId = req.itemId, amount = req.amount) }
//                    ?: run {
                        CartEntity(userId = userId, itemId = req.itemId, amount = req.amount)
                            .let { cartRepository.create(it) }
//                    }
                )
            .let { CartDto.Response.Simple.from(it) }
    }

    @Transactional(readOnly = true)
    suspend fun findCartListByUserId(userId: String): List<CartDto.Response.Simple> =
        cartRepository.findByUserId(userId = userId).map { CartDto.Response.Simple.from(it) }


}