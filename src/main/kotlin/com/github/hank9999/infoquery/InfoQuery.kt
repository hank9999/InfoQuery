package com.github.hank9999.infoquery

import cn.fightingguys.kaiheila.KHL
import cn.fightingguys.kaiheila.KhlBuilder
import cn.fightingguys.kaiheila.event.message.TextMessageEvent
import cn.fightingguys.kaiheila.exception.KhlServiceException
import cn.fightingguys.kaiheila.exception.LoginException
import cn.fightingguys.kaiheila.hook.EventListener
import com.github.hank9999.infoquery.utils.Config
import com.github.hank9999.infoquery.utils.MessageHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

object InfoQuery {

    @JvmStatic
    fun main(args: Array<String>) {
        if (!Config.checkExists()) {
            println("未找到配置文件")
            println("已生成配置文件，请配置后再启动程序")
            exitProcess(1)
        }
        Config.setValue()
        Config.checkConfig()

        val khl = KhlBuilder.builder().createDefault(Config.Bot.token).build()
        khl.addEventListener(object : EventListener() {
            override fun onTextMessageEvent(khl: KHL, event: TextMessageEvent) {
                MessageHandler.textHandler(khl, event)
            }
        })
        try {
            khl.logon()
        } catch (e: LoginException) {
            e.printStackTrace()
        } catch (e: KhlServiceException) {
            e.printStackTrace()
        }
    }
}