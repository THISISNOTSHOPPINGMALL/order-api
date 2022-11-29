package com.shoppingmall.order.service

import com.hindsight.core.exception.GlobalException
import com.hindsight.core.exception.GlobalMessage
import com.shoppingmall.order.domain.CartEntity
import com.shoppingmall.order.dto.CartDto
import com.shoppingmall.order.repository.CartRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class CartService(
    private val cartRepository: CartRepository
) {

    @Transactional
    suspend fun addItemToCart(userId: String, req: CartDto.Request.Add): CartDto.Response.Simple {
        // TODO: ItemId, UserId 검증 로직

        val cartEntity: CartEntity? = cartRepository.findByUserIdAndItemId(userId = userId, itemId = req.itemId)

        return (cartEntity
            ?.updateAmount(amount = req.amount)
            ?: CartEntity(userId = userId, itemId = req.itemId, amount = req.amount)
                .let { cartRepository.save(it) })
            .let { CartDto.Response.Simple.from(it) }
    }

    suspend fun findCartListByUserId(userId: String, offset: Int, limit: Int): List<CartDto.Response.Simple> =
        cartRepository.findAllByUserId(userId = userId, offset = offset, limit = limit)
            .map { CartDto.Response.Simple.from(it) }

    @Transactional
    suspend fun deleteCart(cartId: Long, userId: String): Long =
        cartRepository.findByCartIdAndUserId(cartId = cartId, userId = userId)
            ?.let { cartRepository.save(it.delete()).cartId }
            ?: throw GlobalException(GlobalMessage.NOT_FOUND_CART)


}