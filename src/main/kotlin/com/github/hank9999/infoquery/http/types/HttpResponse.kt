package com.github.hank9999.infoquery.http.types

data class HttpResponse(
    val code: Int = 200,
    val body: String = "",
)
