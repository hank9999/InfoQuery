package com.github.hank9999.infoquery.types

data class HttpResponse(
    val code: Int,
    val response: String,
    val headers: Map<String, String>
)
