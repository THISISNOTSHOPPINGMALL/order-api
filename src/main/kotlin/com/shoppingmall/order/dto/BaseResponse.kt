package com.shoppingmall.order.dto

open class BaseResponse<T>(
    open val data: T
)

data class ListResponse<T>(
    override val data: T,
    val offset: Int,
    val limit: Int,
    val total: Int
): BaseResponse<T>(data)

data class ErrorResponse(
    val name: String,
    val message: String
)