package com.shoppingmall.order.controller

import com.shoppingmall.order.dto.BaseResponse
import com.shoppingmall.order.dto.CartDto
import com.shoppingmall.order.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

private const val SHOP_USER_ID_HEADER_NAME = "x-shop-user-id"

@Controller
@RequestMapping("/api/v1/order/cart")
class CartController(private val cartService: CartService) {

    @PutMapping
    suspend fun addItemToCart(
        @RequestHeader(name = SHOP_USER_ID_HEADER_NAME) userId: String,
        @RequestBody req: CartDto.Request.Add
    ): ResponseEntity<BaseResponse<CartDto.Response.Simple>> =
        cartService.addItemToCart(userId = userId, req = req)
            .let {
                ResponseEntity
                    .ok()
                    .body(BaseResponse(data = it))
            }

    @GetMapping
    suspend fun getCartList(
        @RequestHeader(name = SHOP_USER_ID_HEADER_NAME) userId: String,
        @RequestParam("offset") offset: Int? = 0,
        @RequestParam("limit") limit: Int? = 10,
    ): ResponseEntity<BaseResponse<List<CartDto.Response.Simple>>> =
        cartService.findCartListByUserId(userId = userId, offset = offset ?: 0, limit = limit ?: 10)
            .let {
                ResponseEntity
                    .ok(
                        BaseResponse(it)
                    )
            }

    @DeleteMapping("/{cartId}")
    suspend fun deleteCart(
        @RequestHeader(name = SHOP_USER_ID_HEADER_NAME) userId: String,
        @PathVariable("cartId") cartId: Long
    ): ResponseEntity<BaseResponse<Int>> =
        cartService.deleteCart(cartId = cartId, userId = userId)
            .let {
                ResponseEntity
                    .ok(
                        BaseResponse(it)
                    )
            }

}