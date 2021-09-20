package com.github.hank9999.infoquery.utils

import cn.fightingguys.kaiheila.KHL
import cn.fightingguys.kaiheila.event.message.TextMessageEvent


class MessageHandler {
    companion object {
        fun textHandler(khl: KHL, event: TextMessageEvent) {
            val messageId = event.eventId
//            val author = event.eventAuthorId.id
//            val username = event.eventAuthorId.name
            val guild = event.channel.guild.id
            val channel = event.channel.id
            val channelName = event.channel.name
            val content = event.eventContent
//            var newMessage = "消息类型: 文字消息\n消息ID: $messageId\n发送人: $username#$author\n服务器: $guild\n频道: $channelName#$channel\n"
            var newMessage = "消息类型: 文字消息\n消息ID: $messageId\n服务器: $guild\n频道: $channelName#$channel\n"
            if (event.extra.mention.isNotEmpty()) {
                if (event.extra.mention.find { it.id == Config.Bot.id } == null) {
                    return
                }
//                newMessage += "引用用户: "
//                for (i in event.extra.mention) {
//                    newMessage += ("${i.name}: ${i.id}, ")
//                }
//                newMessage = newMessage.dropLast(2) + "\n"
            } else {
                return
            }
            println(event.extra.mentionRoles)
            if (event.extra.mentionRoles.isNotEmpty()) {
                newMessage += "引用角色: "
                for (i in event.extra.mentionRoles) {
                    val name = event.channel.guild.roles.find { it.id == i.id }?.name
                    newMessage += ("$name#${i.id}, ")
                }
                newMessage = newMessage.dropLast(2) + "\n"
            }
            newMessage += "消息内容: $content"
            event.channel.sendMessage(newMessage, 1, event.eventId, null, null)
        }
    }

}