package com.github.hank9999.infoquery

import com.github.hank9999.kook.Bot
import com.github.hank9999.kook.types.Message
import com.github.hank9999.kook.types.types.ChannelPrivacyTypes
import com.github.hank9999.kook.types.types.MessageTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.fusesource.jansi.AnsiConsole
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

object InfoQuery {

    val logger: Logger = LoggerFactory.getLogger(InfoQuery::class.java)
    lateinit var botUserId: String
    @JvmStatic
    fun main(args: Array<String>) {
        AnsiConsole.systemInstall()
        if (!Config.checkExists()) {
            logger.error("未找到配置文件")
            logger.info("已生成配置文件，请配置后再启动程序")
            exitProcess(1)
        }
        Config.setValue()
        Config.checkConfig()
        val bot = if (Config.Bot.host != null && Config.Bot.host!!.isNotEmpty()) {
            Bot(com.github.hank9999.kook.Config(
                token = Config.Bot.token!!,
                verify_token = Config.Bot.verify_token!!,
                host = Config.Bot.host!!,
                port = Config.Bot.port!!,
                path = Config.Bot.path!!
            ))
        } else {
            Bot(com.github.hank9999.kook.Config(token = Config.Bot.token!!))
        }
        botUserId = bot.botUserId
        bot.registerMessageFunc(MessageTypes.KMD, ChannelPrivacyTypes.GROUP) { msg, cs -> messageHandler(msg, cs) }
    }

    fun messageHandler(msg: Message, cs: CoroutineScope) {
        cs.launch {
            if (!msg.extra.author.bot) {
                msg.extra.mention.find { it == botUserId} ?: return@launch
                val messageId = msg.msgId
                val authorId = msg.authorId
                val username = msg.extra.author.username
                val guildId = msg.extra.guildId
                val channelId = msg.targetId
                val channelName = msg.extra.channelName
                val content = msg.content
                var newMessage =
                    "消息类型: 文字消息\n消息ID: $messageId\n发送者: $username#$authorId\n服务器: $guildId\n频道: $channelName#$channelId\n"
                if (msg.extra.mention.isNotEmpty()) {
                    newMessage += "引用用户: "
                    for (i in msg.extra.kmarkdown.mentionPart) {
                        newMessage += ("${i.fullName}(${i.id}), ")
                    }
                    newMessage = newMessage.dropLast(2) + "\n"
                }
                if (msg.extra.mentionRoles.isNotEmpty()) {
                    newMessage += "引用角色: "
                    for (i in msg.extra.kmarkdown.mentionRolePart) {
                        newMessage += ("${i.name}#${i.roleId}, ")
                    }
                    newMessage = newMessage.dropLast(2) + "\n"
                }
                newMessage += "消息内容: $content"
                var quoteRawMessage = ""
                if (msg.extra.quote.rongId.isNotEmpty()) {
                    val quote = msg.extra.quote
                    newMessage += "\n引用消息ID: ${quote.rongId}\n" +
                            "引用消息发送者: ${quote.author.username}#${quote.author.identifyNum}\n" +
                            "引用消息类型: ${quote.type}"
                    quoteRawMessage = "\n引用消息内容: ${quote.content}"
                }
                //logger.info(newMessage.replace("\n", "; "))
                try {
                    msg.reply(newMessage + quoteRawMessage)
                } catch (ex: Exception) {
                    logger.error("${ex.javaClass.name} ${ex.message}\n${ex.stackTraceToString()}")
                    msg.reply(newMessage + "\n引用消息内容发送失败: ${ex.message.toString()}")
                }
            }
        }
    }
}