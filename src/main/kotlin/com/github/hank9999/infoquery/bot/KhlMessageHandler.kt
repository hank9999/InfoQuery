package com.github.hank9999.infoquery.bot

import com.github.hank9999.infoquery.http.HttpApi
import com.github.hank9999.infoquery.bot.types.KhlMessage
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import com.github.hank9999.infoquery.json.JSON.Companion.json
import com.github.hank9999.infoquery.bot.types.Quote
import com.github.hank9999.infoquery.bot.types.types.MessageTypes
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KhlMessageHandler {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(KhlMessageHandler::class.java)

        fun messageHandler(element: JsonElement) {
            val data = json.decodeFromJsonElement<KhlMessage>(element)
            if (data.type == MessageTypes.KMD && !data.extra.author.bot) {
                data.extra.mention.find { it == Bot.me.id} ?: return
                val messageId = data.msg_id
                val authorId = data.author_id
                val username = data.extra.author.username
                val guildId = data.extra.guild_id
                val channelId = data.target_id
                val channelName = data.extra.channel_name
                val content = data.content
                var newMessage = "消息类型: 文字消息\n消息ID: $messageId\n发送者: $username#$authorId\n服务器: $guildId\n频道: $channelName#$channelId\n"
                if (data.extra.mention.isNotEmpty()) {
                    newMessage += "引用用户: "
                    for (i in data.extra.kmarkdown.mention_part) {
                        newMessage += ("${i.full_name}, ")
                    }
                    newMessage = newMessage.dropLast(2) + "\n"
                }
                if (data.extra.mention_roles.isNotEmpty()) {
                    newMessage += "引用角色: "
                    for (i in data.extra.kmarkdown.mention_role_part) {
                        newMessage += ("${i.name}#${i.role_id}, ")
                    }
                    newMessage = newMessage.dropLast(2) + "\n"
                }
                newMessage += "消息内容: $content"
                if (data.extra.quote != Quote()) {
                    val quote = data.extra.quote
                    newMessage += "\n引用消息ID: ${quote.rong_id}\n"
                    newMessage += "引用消息发送者: ${quote.author.username}#${quote.author.identify_num}\n"
                    newMessage += "引用消息类型: ${quote.type}\n"
                    newMessage += "引用消息内容: ${quote.content}"
                }
                logger.info(newMessage.replace("\n", "; "))
                HttpApi.Channel.create(newMessage, data.target_id, quote = data.msg_id)
            }
        }

        fun systemHandler(element: JsonElement) {
            // TODO()
        }
    }
}