package com.github.hank9999.infoquery.kaiheila.types

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageTextExtra(
    val d: D

) {
    data class D(
        val extra: Extra
    ) {
        data class Extra(
            val author: Author,
            val channel_name: String,
            val guild_id: String,
            val mention: List<String>,
            val mention_roles: List<Int>,
            val mention_all: Boolean,
            val mention_here: Boolean
        )
    }
}