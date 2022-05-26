package com.github.hank9999.infoquery.bot.types.kmd

import kotlinx.serialization.Serializable

@Serializable
data class MentionRolePart(
    val role_id: Int = 0,
    val name: String = ""
)