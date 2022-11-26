package com.shoppingmall.order.service

import com.hindsight.core.exception.GlobalException
import com.hindsight.core.exception.GlobalMessage
import com.shoppingmall.order.domain.OrderEntity
import com.shoppingmall.order.domain.OrderItemEntity
import com.shoppingmall.order.domain.OrderStatus
import com.shoppingmall.order.dto.OrderDto
import com.shoppingmall.order.repository.OrderItemRepository
import com.shoppingmall.order.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepository: OrderRepository,
    private val itemOfOrderRepository: OrderItemRepository
) {

    @Transactional
    suspend fun create(userId: String, req: OrderDto.Request.Add): OrderDto.Response.Simple {
        val order = OrderEntity(
            userId = userId,
            status = OrderStatus.BEFORE_PAYMENT,
            address = req.address
        ).also {
            orderRepository.create(it)
        }

        req.itemIdList.map {
            OrderItemEntity(
                orderId = order.orderId!!,
                itemId = it.itemId,
                // TODO: 추후 ITEM 조회해 저장
                price = it.price,
                amount = it.amount
            )
        }.forEach {
            try {
                itemOfOrderRepository.create(it)
            } catch (e: Exception) {
                Unit
            }
        }

        return orderRepository.findAllByUserId(userId = userId, limit = 1, offset = 0).first()
            .let {
                OrderDto.Response.Simple.from(order = it)
            }

    }

    suspend fun findAllByUserId(userId: String, limit: Int, offset: Int): List<OrderDto.Response.Simple> =
        orderRepository.findAllByUserId(userId = userId, limit = limit, offset = offset)
            .map(OrderDto.Response.Simple::from)


    suspend fun findByOrderId(orderId: Long, userId: String): OrderDto.Response.Simple =
        orderRepository.findByOrderIdAndUserId(orderId = orderId, userId = userId)
            ?.let { OrderDto.Response.Simple.from(it) }
            ?: throw GlobalException(GlobalMessage.NOT_FOUND_ORDER)


}