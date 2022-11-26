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

        return if (cartEntity == null) {
            CartEntity(userId = userId, itemId = req.itemId, amount = req.amount)
                .let {
                    cartRepository.create(it)
//                    try { // TODO: 쿼리는 나가지만, 응답 값을 받는 데에서 익셉션 발생 추후 처리
//                        cartRepository.create(it)
//                    } catch (e: Exception) {
//                        cartRepository.findByUserIdAndItemId(userId = userId, itemId = req.itemId)
//                            ?: throw GlobalException(GlobalMessage.NOT_FOUND_CART)
//                    }
                }
        } else {
            cartRepository.update(userId = userId, itemId = req.itemId, amount = req.amount)
            cartEntity.updateAmount(amount = req.amount)
        }.let {
            CartDto.Response.Simple.from(it)
        }
    }

    @Transactional(readOnly = true)
    suspend fun findCartListByUserId(userId: String, offset: Int, limit: Int): List<CartDto.Response.Simple> =
        cartRepository.findAllByUserId(userId = userId, offset = offset, limit = limit)
            .map { CartDto.Response.Simple.from(it) }

    @Transactional
    suspend fun deleteCart(cartId: Long, userId: String): Int =
        cartRepository.findByCartIdAndUserId(cartId = cartId, userId = userId)
            ?.let { cartRepository.delete(cartId = cartId) }
            ?: throw GlobalException(GlobalMessage.NOT_FOUND_CART)


}