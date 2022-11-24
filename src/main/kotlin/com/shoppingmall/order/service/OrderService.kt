package com.shoppingmall.order.service

import com.hindsight.core.exception.GlobalException
import com.hindsight.core.exception.GlobalMessage
import com.shoppingmall.order.domain.ItemOfOrderEntity
import com.shoppingmall.order.domain.OrderEntity
import com.shoppingmall.order.domain.OrderStatus
import com.shoppingmall.order.dto.ItemOfOrderDto
import com.shoppingmall.order.dto.OrderDto
import com.shoppingmall.order.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepository: OrderRepository,
) {

    @Transactional
    suspend fun create(userId: String, req: OrderDto.Request.Add): OrderDto.Response.Simple {
        val order: OrderEntity = OrderEntity(
            userId = userId,
            status = OrderStatus.BEFORE_PAYMENT,
            address = req.address
        )
            .let {
                try {
                    orderRepository.create(it)
                } catch (e: Exception) { // TODO: jdsl의 create에서 exception이 발생해 추후 처리 예정
                    orderRepository.findAllByUserId(userId = userId, limit = 1, offset = 0).first()
                }
            }

        val itemList = req.itemIdList.map {
            ItemOfOrderEntity(
                orderId = order.orderId!!,
                itemId = it,
                // TODO: 추후 ITEM 조회해 저장
                price = 1,
                amount = 1
            )
        }.forEach {
            try {
                itemOfOrderRepository.create(it)
            } catch (e: Exception) {
                Unit
            }
        }.let {
            itemOfOrderRepository.findAllByOrderId(orderId = order.orderId!!)
        }.map { ItemOfOrderDto.Response.Simple.from(entity = it) }

        return OrderDto.Response.Simple.from(order = order, itemList = itemList)
    }

    suspend fun findAllByUserId(userId: String, limit: Int, offset: Int): List<OrderDto.Response.Simple> {
        orderRepository.findAllByUserId(userId = userId, limit = limit, offset = offset)
            .map {
                OrderDto.Response.Simple::from
            }
    }


    suspend fun findByOrderId(orderId: Long, userId: String): OrderDto.Response.Simple =
        orderRepository.findByOrderIdAndUserId(orderId = orderId, userId = userId)
            ?.let { OrderDto.Response.Simple.from(it) }
            ?: throw GlobalException(GlobalMessage.NOT_FOUND_ORDER)


}