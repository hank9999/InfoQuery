package com.github.hank9999.infoquery.kaiheila.utils

import com.github.hank9999.infoquery.kaiheila.Hardcore
import com.github.hank9999.infoquery.kaiheila.KaiheilaWs
import com.github.hank9999.infoquery.kaiheila.types.Message
import com.github.hank9999.infoquery.kaiheila.types.MessageTextExtra
import com.github.hank9999.infoquery.kaiheila.types.RoleList
import com.github.hank9999.infoquery.utils.Config
import com.github.hank9999.infoquery.utils.Http
import com.github.hank9999.infoquery.utils.Json
import kaiheila.types.MessageCreate

class MessageHandler {
    fun onEvent(text: String) {
        val message = Json.deserialize(text, Message::class)
        message?.let {
            if (message.d.type == 1 && message.d.channel_type == "GROUP") {
                val extra = Json.deserialize(text, MessageTextExtra::class)
                extra?.let {
                    val messageId = message.d.msg_id
                    val author = message.d.author_id
                    val username = extra.d.extra.author.username
                    val guild = extra.d.extra.guild_id
                    val channel = message.d.target_id
                    val channelName = extra.d.extra.channel_name
                    val content = message.d.content

                    var newMessage = "消息类型: 文字消息\n消息ID: $messageId\n发送人: $username#$author\n服务器: $guild\n频道: $channelName#$channel\n"
                    if (extra.d.extra.mention.isNotEmpty()) {
                        if (!extra.d.extra.mention.contains(Config.Bot.id)) {
                            return
                        }
                        newMessage += "引用用户: "
                        for (i in extra.d.extra.mention) {
                            val b = content.dropLast(content.length - content.indexOf("#$i"))
                            val name = (b.substring(b.lastIndexOf('@') + 1))
                            newMessage += ("$name#$i, ")
                        }
                        newMessage = newMessage.dropLast(2) + "\n"
                    } else {
                        return
                    }
                    if (extra.d.extra.mention_roles.isNotEmpty()) {
                        val resp = Http.get(
                            Hardcore.Api.Role.guildList,
                            mutableMapOf<String, String>().apply { this["Authorization"] = "Bot " + Config.Bot.token },
                            mutableMapOf<String, String>().apply { this["guild_id"] = guild }
                        )
                        val roleLists = Json.deserialize(resp.response, RoleList::class)
                        newMessage += "引用角色: "
                        for (i in extra.d.extra.mention_roles) {
                            val name = roleLists?.data?.items?.find { it.role_id == i }?.name
                            newMessage += ("$name#$i, ")
                        }
                        newMessage = newMessage.dropLast(2) + "\n"
                    }
                    val jsonData = Json.serialize(MessageCreate(target_id = channel, content = newMessage, quote = messageId))
                    jsonData?.let {
                        Http.post(
                            Hardcore.Api.Message.creareUrl,
                            mutableMapOf<String, String>().apply { this["Authorization"] = "Bot " + Config.Bot.token },
                            jsonData,
                            "application/json; charset=utf-8"
                        )
                        KaiheilaWs.logger.info(newMessage.replace("\n", "; "))
                    }
                }
            }
        }

    }
}