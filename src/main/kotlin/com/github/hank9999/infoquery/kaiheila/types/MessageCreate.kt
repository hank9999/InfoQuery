package kaiheila.types

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageCreate (
    val type: Int = 1,
    val target_id: String,
    val content: String,
    val quote: String = "",
    val nonce: String = "",
    val temp_target_id: String = ""
)
