package com.hindsight.core.exception

import org.springframework.http.HttpStatus

interface IGlobalMessage {
    val status: HttpStatus
    val message: String

    fun getName(): String
}

open class GlobalException(val globalMessage: IGlobalMessage) : RuntimeException()

enum class GlobalMessage(
    override val status: HttpStatus,
    override val message: String
) : IGlobalMessage {

    TEST_EXCEPTION(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알수없는 에러가 발생했습니다."),
    REQUEST_NOT_VALID(HttpStatus.BAD_REQUEST, "요청이 잘못되었습니다."),
    ALREADY_EXIST_ITEM_IN_CART(HttpStatus.CONFLICT, "장바구니에 이미 존재하는 상품입니다."),

    // CART
    NOT_FOUND_CART(HttpStatus.NOT_FOUND, "해당 회원 아이디와 상품 아이디의 장바구니가 없습니다."),

    // ORDER
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "해당하는 아이디의 주문건이 없습니다."),


    ;

    override fun getName(): String = this.name

}