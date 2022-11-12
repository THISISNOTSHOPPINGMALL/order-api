package com.shoppingmall.order.exception

import com.hindsight.core.exception.IGlobalMessage
import org.springframework.http.HttpStatus

enum class OrderMessage(
    override val status: HttpStatus,
    override val message: String
) : IGlobalMessage {

    // ITEM
    ITEM_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "상품 서버와 연결을 실패했습니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다."),

    // CART
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."),
    CART_CREATE_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "장바구니에 상품 생성중 오류가 발생했습니다."),
    ;

    override fun getName(): String = this.name
}