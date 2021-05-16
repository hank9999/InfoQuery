package com.github.hank9999.infoquery.kaiheila.types

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Author(
    val avatar: String,
    val id: String,
    val identify_num: String,
    val nickname: String,
    val roles: List<Int>,
    val username: String?
)