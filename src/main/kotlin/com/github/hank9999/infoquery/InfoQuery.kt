package com.github.hank9999.infoquery


import com.github.hank9999.infoquery.bot.Bot
import com.github.hank9999.infoquery.bot.WebHook
import org.fusesource.jansi.AnsiConsole
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

object InfoQuery {

    val logger: Logger = LoggerFactory.getLogger(InfoQuery::class.java)
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
        Bot.fetchMe()
        WebHook().initialize(Config.Bot.host!!, Config.Bot.port!!, Config.Bot.path!!)
    }
}