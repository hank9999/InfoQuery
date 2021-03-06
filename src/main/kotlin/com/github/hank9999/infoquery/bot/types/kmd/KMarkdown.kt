package com.github.hank9999.infoquery.bot.types.kmd

import kotlinx.serialization.Serializable

@Serializable
data class KMarkdown(
    val raw_content: String = "",
    val mention_part: List<MentionPart> = emptyList(),
    val mention_role_part: List<MentionRolePart> = emptyList()
)