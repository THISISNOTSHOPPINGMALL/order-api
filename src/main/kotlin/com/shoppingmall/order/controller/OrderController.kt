package com.shoppingmall.order.controller

import com.shoppingmall.order.dto.BaseResponse
import com.shoppingmall.order.dto.OrderDto
import com.shoppingmall.order.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

private const val SHOP_USER_ID_HEADER_NAME = "x-shop-user-id"

@Controller
@RequestMapping("/api/v1/order")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    suspend fun create(
        @RequestHeader(name = SHOP_USER_ID_HEADER_NAME) userId: String,
        @RequestBody req: OrderDto.Request.Add
    ): ResponseEntity<BaseResponse<OrderDto.Response.Simple>> =
        orderService.create(
            userId = userId,
            req = req
        ).let { ok().body(BaseResponse(it)) }

    @GetMapping
    suspend fun findAllByUserId(
        @RequestHeader(name = SHOP_USER_ID_HEADER_NAME) userId: String,
        @RequestParam("offset") offset: Int? = 0,
        @RequestParam("limit") limit: Int? = 10
    ): ResponseEntity<BaseResponse<List<OrderDto.Response.Simple>>> =
        orderService.findAllByUserId(userId = userId, offset = offset ?: 0, limit = limit ?: 10)
            .let {
                ok().body(
                    BaseResponse(it)
                )
            }

    @GetMapping("/{orderId}")
    suspend fun findByOrderIdAndUserId(
        @RequestHeader(name = SHOP_USER_ID_HEADER_NAME) userId: String,
        @PathVariable("orderId") orderId: Long
    ): ResponseEntity<BaseResponse<OrderDto.Response.Simple>> =
        orderService.findByOrderId(orderId = orderId, userId = userId)
            .let {
                ok().body(
                    BaseResponse(it)
                )
            }

}